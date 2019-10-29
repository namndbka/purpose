package vn.zuni.findpurpose.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.v7.widget.AppCompatImageButton
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.MediaController
import android.widget.RelativeLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_video.*
import kotlinx.android.synthetic.main.layout_preview.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import vn.zuni.findpurpose.*
import vn.zuni.findpurpose.api.ISurveyApi
import vn.zuni.findpurpose.extensions.*
import vn.zuni.findpurpose.models.VideoRequest
import java.io.File
import javax.inject.Inject

/**
 *
 * Created by namnd on 01-Jan-18.
 */

class FragmentVideo : BaseFragment() {

    private var questionIndex: Int = 0
    private var surveyId: Int = 0
    private var mFileUri: Uri? = null
    private var mVisibleToUser: Boolean = false
    private var mFragCreated: Boolean = false
    @Inject
    lateinit var surveyApi: ISurveyApi

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        SurveyApplication.instance.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(L.fragment_video, container, false)
    }

    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mFragCreated = true
        surveyId = mSurveyProperties?.id ?: 0
        questionIndex = mQuestionData?.index ?: 0
        textview_q_title.text = Html.fromHtml(mQuestionData?.questionTitle)
        context?.let {
            val videoController = MyMediaController(this@FragmentVideo, controllerAnchor)
            videoPreview.setMediaController(videoController)
            var filePath = it.getVideo(surveyId, questionIndex)
            if (filePath.isNotEmpty() || File(filePath).exists()) {
                mFileUri = Uri.parse(filePath)
                previewLayout.show()
                recordBtn.hide()
                if (mQuestionData?.index == 0) {
                    previewMedia(mFileUri)
                }
            } else {
                val fileUrl = it.getAnswer(surveyId, questionIndex)
                if (fileUrl.isNotEmpty()) {
                    filePath = fileUrl
                    mFileUri = Uri.parse(filePath)
                    previewLayout.show()
                    recordBtn.hide()
                    if (mQuestionData?.index == 0) {
                        previewMedia(mFileUri)
                    }
                }
            }
        }
        recordBtn.onClick { recordVideo() }
        recordAgainBtn.onClick { recordVideo() }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisible) {
            mVisibleToUser = isVisibleToUser
            if (isVisibleToUser) {
                previewMedia(mFileUri)
            } else {
                if (videoPreview != null && videoPreview.isPlaying) videoPreview.stopPlayback()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("file_uri", mFileUri)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val uri: Uri? = savedInstanceState?.getParcelable("file_uri")
        uri?.let { mFileUri = uri }
    }

    private fun uploadVideo(videoPath: String) {
        uploadProgressBar.show()
        val videoFile = File(videoPath)
        val videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile)
        val videoPart = MultipartBody.Part.createFormData("video", videoFile.name, videoBody)
        surveyApi.uploadVideo(videoPart).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(ThreadHelper.instance.scheduler)
                .subscribe({ onUploadSuccess(it) }, { t -> onUploadFailed(t) })
    }

    private fun onUploadSuccess(videoRequest: VideoRequest) {
        uploadProgressBar.hide()
        if (videoRequest.error == null) {
            context?.saveAnswer(surveyId, questionIndex, "${WEB_URL}${videoRequest.msg}")
            context?.showAlert(S.end_text_title, false) {}
        } else {
            Log.e("UPLOADVIDEO", "error: " + videoRequest.error)
        }

    }

    private fun onUploadFailed(throwable: Throwable) {
        uploadProgressBar.hide()
        Log.e("UPLOADVIDEO", throwable.localizedMessage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Handler().post {
                        previewLayout.show(true)
                        recordBtn.hide()
                    }
                    mFileUri?.let {
                        uploadVideo(it.path)
                        previewMedia(it)
                        context?.saveVideo(surveyId, questionIndex, it.path)
                        (activity as SurveyActivity).setResult(Activity.RESULT_OK)
                    }
                    content.post({ content.fullScroll(View.FOCUS_DOWN) });
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Launching camera app to record video
     */
    private fun recordVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        mFileUri = getOutputMediaFileUri()
        // set video quality
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 300)
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri)
        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE)
    }

    /**
     * Creating file uri to store image/video
     */
    private fun getOutputMediaFileUri(): Uri {
        return Uri.fromFile(getOutputMediaFile())
    }

    /**
     * returning image / video
     */
    private fun getOutputMediaFile(): File? {
        // External sdcard location
        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), VIDEO_DIRECTORY_NAME)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null
        }
        val email = context?.getEmail()?.replace("@", "_")?.replace(".", "_") ?: emptyString
        // Create a media file name
        val fileName = "${email}_${surveyId}_${mSurveyProperties?.keyword}_$questionIndex"
        return File(mediaStorageDir.path + File.separator + fileName + ".mp4")
    }

    /**
     * Displaying captured image/video on the screen
     */
    private fun previewMedia(fileUri: Uri?) {
        fileUri?.let {
            videoPreview.setVideoURI(it)
            videoPreview.start()
        }
    }

    companion object {
        private val CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 201
        // Directory name to store captured images and videos
        val VIDEO_DIRECTORY_NAME = "eSurveyUpload"
        val WEB_URL = "http://esurvey.zuni.vn"
    }

    @SuppressLint("ViewConstructor")
    class MyMediaController(private var fragment: FragmentVideo, private var anchorView: FrameLayout) : MediaController(fragment.context) {

        override fun setAnchorView(view: View?) {
            super.setAnchorView(view)
            val fullScreen = AppCompatImageButton(context)
            fullScreen.setImageResource(R.drawable.ic_fullscreen)
            val params = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            params.gravity =  Gravity.START or Gravity.END
            addView(fullScreen, params)
            fullScreen.onClick {
                val uri = fragment.mFileUri
                uri?.let {
                    val intent = Intent(Intent.ACTION_VIEW, it);
                    intent.setDataAndType(it, "video/mp4");
                    context.startActivity(intent);
                }
            }
        }
        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)
            val lp = anchorView.layoutParams as RelativeLayout.LayoutParams
            lp.setMargins(0, 0, 0, h)
            anchorView.layoutParams = lp
            anchorView.requestLayout()
        }
    }
}