<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Carbon\Carbon;
use Redirect;
use Auth;
use DB;
use Config;
use App\Models\Survey;
use Excel;

class DashboardController extends Controller
{
	 protected $active = 'dashboard';

    public function __construct()
    {
        $this->middleware('auth');
    }

	public function index()
    {
        $surveys = Survey::all();
    	return view('survey', ['surveys'=> $surveys, 'active' => $this->active]);	
    }

    public function exportUserSurvey(Request $request){
        $type = $request->get('type');
        $surveys = Survey::all();
        $result = array();
        foreach ($surveys as $survey) {
            $useSurvey = array();
            $useSurvey["Email"] = $survey->email;
            $useSurvey["Completion"] = $survey->completed . "%";
            $answers = json_decode($survey->answers, true);
            $index = 0;
            foreach ($answers as $answer) {
                if($answer["sid"] == 0) { continue; }
                if(isset($answer["answers"])) {
                    $ans = json_decode($answer["answers"], true);
                    if(sizeof($ans) > 1) {
                        foreach (range(0, sizeof($ans)-1) as $i) {
                            $useSurvey[$answer["sid"].'_'.($i+1)] = $ans[$i];
                        } 
                    } else if(sizeof($ans) == 1) {
                        $useSurvey[$answer["sid"]] = $ans["0"];
                    } else {
                        $useSurvey[$answer["sid"]] = "";
                    }

                }
                $index += 1;
                
            }
            array_push($result, $useSurvey);
        }
        $fileName = $subPath = Carbon::now()->format('Y_m_d');
        return Excel::create('surveys_'.$fileName, function($excel) use($fileName, $result){
             // Set the title
            $excel->setTitle('Find Purpose');
            // Chain the setters
            $excel->setCreator('zuni.vn')->setCompany('Công Ty Cổ Phần Công Nghệ Giáo Dục Zuni.');
            // Call them separately
            // $excel->setDescription('');
            $excel->sheet($fileName,function($sheet) use($result){
                $sheet->setPageMargin(2.25);
                $sheet->setFontSize(16);
                $sheet->fromArray($result);
                $sheet->row(1, function($row) {
                    // call cell manipulation methods
                    $row->setBackground('#FFFF00');
                });
            });
        })->download($type);
    }
}