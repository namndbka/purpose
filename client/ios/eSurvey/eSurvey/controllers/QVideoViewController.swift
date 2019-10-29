//
//  QVideoViewController.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/14/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import UIKit
import GTProgressBar
import MobileCoreServices
import AVFoundation

class QVideoViewController: UIViewController {
    private let WEB_URL = "http://\(ISurveyApi.HOSTNAME)"
    var mQuestion: Question!
    var mSurveyProperties: SurveyProperties!
    var mQuestionsCount: Int = 0
    @IBOutlet weak var questionContent : UITextView!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var nextButton: UIButton!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var noteLabel: UILabel!
    @IBOutlet weak var recordButton: UIButton!
    @IBOutlet weak var videoView: UIVideoPreview!
    @IBOutlet weak var headerView: UIHeaderView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.recordButton.setTitle(Phrase.from("record_video").description.uppercased(), for: .normal)
        self.questionContent.attributedText = mQuestion.questionTitle.htmlAttributedString()
        self.titleLabel.text = String(mQuestion.index + 1) + "/" + String(mQuestionsCount)
        self.updateBottomView()
        self.updateHeaderView()
        self.questionContent.sizeToFit()
        self.loadDataView()
    }
    
    private func loadDataView(){
        let saveVideo = DataManager.getVideo(mSurveyProperties.id, questionIndex: mQuestion.index)
        if(mSurveyProperties.id == 5) { self.videoView.nextButton.isHidden = true }
        self.videoView.isHidden = true
        self.recordButton.isHidden = false
        self.noteLabel.isHidden = false
        var offlinePlay = false
        if(!saveVideo.isEmpty) {
                if FileManager.default.fileExists(atPath: saveVideo) {
                    let videoUrl = URL(fileURLWithPath: saveVideo)
                    offlinePlay = true
                    playVideo(videoUrl)
                } else {
                   
                }
        }
        if(!offlinePlay){
            let urlVideoStr = DataManager.getAnswer(mSurveyProperties.id, questionIndex: mQuestion.index)
            if(!urlVideoStr.isEmpty){
                let videoUrl = URL(fileURLWithPath: urlVideoStr)
                playVideo(videoUrl)
            }
        }
        self.videoView.recordAgainClick(self, action: #selector(self.recordActionClicked))
        self.videoView.nextQuestionClick(self, action: #selector(self.nextQuestionClick))
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        self.updateHeaderProgress()
    }
    
    private func updateHeaderProgress(){
        var progress = 0
        for index in 0..<mQuestionsCount {
            let ans = DataManager.getAnswer(mSurveyProperties.id, questionIndex: index)
            if(!ans.isEmpty) { progress += 1 }
        }
        self.headerView.progress = CGFloat(progress)/CGFloat(mQuestionsCount)
    }

    private func updateHeaderView(){
        self.headerView.indexText = String(mSurveyProperties.id)
        self.headerView.titleText = mSurveyProperties.title
        self.headerView.progress = 0.4
        self.headerView.homeClick(self, action: #selector(self.homeActionClicked))
    }
    
    private func updateBottomView() {
        let backBtnHide = (mQuestion.index == 0)
        let nextBtnHide = (mQuestion.index >= (mQuestionsCount - 1))
        if(backBtnHide && !nextBtnHide) {
            self.titleLabel.padding = UIEdgeInsets(top: 0, left: 70, bottom: 0, right: 0)
        }
        if(!backBtnHide && nextBtnHide) {
            self.titleLabel.padding = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 70)
        }
        self.backButton.isHidden = backBtnHide
        self.nextButton.isHidden = nextBtnHide
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func backActionClicked() {
        let parentView = self.parent as! SurveyViewController
        let index = mQuestion.index - 1
        parentView.setViewControllers([parentView.mOrderedViewControllers[index]], direction: .reverse, animated: true, completion: nil)
    }
    
    @IBAction func nextActionClicked() {
        let parentView = self.parent as! SurveyViewController
        let index = mQuestion.index + 1
        parentView.setViewControllers([parentView.mOrderedViewControllers[index]], direction: .forward, animated: true, completion: nil)
    }
    
    @IBAction func nextQuestionClick(){
        let parentView = self.parent as! SurveyViewController
        parentView.nextSurveyLoad()
    
    }
    @IBAction func homeActionClicked() {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func recordActionClicked() {
        if UIImagePickerController.isSourceTypeAvailable(.camera) == false {
            return
        }
        let picker = UIImagePickerController()
        picker.sourceType = .camera
        picker.mediaTypes = [kUTTypeMovie as NSString as String]
        picker.allowsEditing = true
        picker.videoMaximumDuration = 300 // 5 minutes
        picker.videoQuality = .type640x480
        picker.delegate = self
        present(picker, animated: true, completion: nil)
    }
    
    func playVideo(_ videoUrl: URL) {
        self.videoView.isHidden = false
        self.recordButton.isHidden = true
        self.noteLabel.isHidden = true
        self.videoView.videoUrl = videoUrl
    }
}

extension QVideoViewController: UIImagePickerControllerDelegate {
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        let mediaType = info[UIImagePickerControllerMediaType] as! NSString
        dismiss(animated: true, completion: nil)
        if mediaType == kUTTypeMovie {
            let urlVideo = info[UIImagePickerControllerMediaURL] as! URL
            if UIVideoAtPathIsCompatibleWithSavedPhotosAlbum(urlVideo.path) {
                playVideo(urlVideo)
                encodeVideo(urlVideo)
            }
        }
    }
    func encodeVideo(_ videoURL: URL)  {
        let avAsset = AVURLAsset(url: videoURL, options: nil)
        let email = DataManager.getEmail()?.replacingOccurrences(of: "@", with: "_").replacingOccurrences(of: ".", with: "_")
        let fileName = "\(email!)_\(mSurveyProperties.id)_\(mSurveyProperties?.keyword! ?? "")_\(mQuestion.index)"
        //Create Export session
        let exportSession = AVAssetExportSession(asset: avAsset, presetName: AVAssetExportPreset640x480)
        let documentsDirectory2 = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0] as URL
        let filePath = documentsDirectory2.appendingPathComponent("\(fileName).mp4")
        deleteFile(filePath)
        exportSession!.outputURL = filePath
        exportSession!.outputFileType = AVFileType.mp4
        exportSession!.shouldOptimizeForNetworkUse = false
        exportSession!.exportAsynchronously(completionHandler: {() -> Void in
            switch exportSession!.status {
            case .failed:
                print("%@",exportSession?.error ?? "empty0")
                break
            case .cancelled:
                print("Export canceled")
                break
            case .completed:
                //Video conversion finished
                self.uploadVideo((exportSession?.outputURL)!)
                break
            default:
                break
            }
        })
    }
    func deleteFile(_ filePath:URL) {
        guard FileManager.default.fileExists(atPath: filePath.path) else { return }
        do { try FileManager.default.removeItem(atPath: filePath.path)}
        catch { print("Unable to delete file: \(error) : \(#function).") }
    }
    private func uploadVideo(_ videoUrl: URL) {
        DataManager.saveVideo(self.mSurveyProperties.id, questionIndex: self.mQuestion.index, url:videoUrl.path)
        ISurveyApi.uploadVideo(videoUrl, onProgress: { (progress) in
            if(progress > 0.0 && progress < 1.0 ) { self.videoView.progressView.isHidden = false }
            self.videoView.progress = Float(progress)
            if(progress >= 1.0 ) { self.videoView.progressHide() }
        }, onSuccess: {(message) in
            if(message.error == nil){
                DataManager.needPostAnswer()
                DataManager.saveAnswer(self.mSurveyProperties.id, questionIndex: self.mQuestion.index, answer: "\(self.WEB_URL)\(message.msg!)")
                self.showAlert(Phrase.from("answer_sent").description)
            }else {
                print(message.error)
            }
        }) { (error) in print(error.localizedDescription)}
    }
    
    private func showAlert(_ msg: String) {
        let alert = UIAlertController(title: "", message: msg, preferredStyle: UIAlertControllerStyle.alert)
        alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
}

extension QVideoViewController: UINavigationControllerDelegate {
}
