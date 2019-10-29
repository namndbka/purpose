<?php

namespace App\Http\Controllers\Api;

use Illuminate\Http\Request;
use Laravel\Passport\Token;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Route;
use App\Models\Survey;
use App\Models\NotificationTopic;
use Response;
use Config;
use Hash;
use Auth;
use DB;
use Carbon\Carbon;

class SurveyController extends Controller
{

    public function subcribeEmail(Request $request){
        $email = $request->get('email');
        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            $emailErr = "Invalid email format"; 
            return Response::json(['error'=>$emailErr]);
        }
        $survey = Survey::whereEmail($email)->first();
        if(!$survey) {
            $survey = Survey::create(['email' => $email]);
        }
        $survey->answers = json_decode($survey->answers, true);
        return Response::json($survey);
    }

    public function answerSurvey(Request $request){
        $email = $request->get('email');
        $answers = $request->get('answers');
        $completed = $request->get("completed");
        // $ans = (array)json_decode($answers, true);
        if($answers && $email){
            $survey = Survey::firstOrCreate(['email' => $email]);
            $survey->answers = $answers;
            $survey->completed = $completed;
            $survey->save();
            $survey->answers = json_decode($survey->answers, true);
            return Response::json($survey);
        }
        return Response::json(['error'=>'not enough data']);
    }


    public function uploadVideo(Request $request){
        if($request->hasFile('video')){
            $video = $request->file('video');
            $mime = $video->getMimeType();
            $subPath = Carbon::now()->format('Y/m/d');
            if ($mime == "video/mp4" || $mime = "video/quicktime") {
                $fileName = $video->getClientOriginalName();
                $pathVideo = 'upload/videos/'.$subPath;
                $destinationPath = public_path($pathVideo);
                if(!file_exists($destinationPath) && !is_dir($destinationPath)){
                    mkdir($destinationPath, 0777, true);
                }
                $video->move($destinationPath, $fileName);
                $url = '/' . $pathVideo . '/' . $fileName;
                return Response::json(['msg'=> $url]);
            }
            return Response::json(['error'=> $mime]);
        }
        return Response::json(['error'=> 'Failed']);
    }

    /* ================ NOTIFICATION ============================== */
    /**
     * @SWG\Post(
     *   path="/notification/topic/list",
     *   tags={"Announcement"},
     *   description="get subscribed topics",
     *   summary="This can only be done by the logged in user.",
     *   operationId="getSubscribedTopics",
     *   @SWG\Parameter(
     *         description="device_token",
     *         in="formData",
     *         name="device_token",
     *         required=true,
     *         type="string",
     *   ),
     *   produces={"application/json"},
     *   @SWG\Response(
     *     response=200,
     *     description="successful operation",
     *     @SWG\Schema(
     *      type="array",
     *     @SWG\Items(
     *     type="object",
     *      @SWG\Property(property="user_id", type="integer")
     *      ),
     *     ),
     *   ),
     * )
     */
    public function getSubscribedTopic(Request $request) {
        $deviceToken = $request->get('device_token');
        return NotificationTopic::where('device_token', '=', $deviceToken)->pluck('user_id');
    }
    /**
     * @SWG\Post(
     *   path="/notification/topic/subscribe",
     *   tags={"Announcement"},
     *   description="subscribed topics",
     *   summary="This can only be done by the logged in user.",
     *   operationId="subscribeTopics",
     *   @SWG\Parameter(
     *         description="device token",
     *         in="formData",
     *         name="device_token",
     *         required=true,
     *         type="string",
     *   ),
     *   @SWG\Parameter(
     *         description="topic names",
     *         in="formData",
     *         name="topic_names[]",
     *         required=true,
     *         type="array",
     *         @SWG\Items(type="string"),
     *         collectionFormat="csv"
     *   ),
     *   @SWG\Parameter(
     *         description="platform",
     *         in="formData",
     *         name="platform",
     *         required=true,
     *         type="string",
     *   ),
     *   produces={"application/json"},
     *   @SWG\Response(
     *     response=200,
     *     description="successful operation",
     *     @SWG\Schema(
     *     type="array",
     *     @SWG\Items(
     *     type="object",
     *      @SWG\Property(property="error", type="string")
     *      ),
     *     ),
     *   ),
     * )
     */
    public function subscribeTopic(Request $request) {
        $userId = Auth::user()->id;
        $deviceToken = $request->get('device_token');
        if($_SERVER['HTTP_FROM'] == 'androi_parent' || $_SERVER['HTTP_FROM'] == 'androi_teacher'){  
            $platform = "android";
        } else if($_SERVER['HTTP_FROM'] == 'ios_parent' || $_SERVER['HTTP_FROM'] == 'ios_teacher'){
            $platform = "ios";
        } else {
            return Response::json(['error' => 'header_not_accept']);
        }
        if(empty($deviceToken) || empty($userId)) {
            $result = "device_token_empty_or_user_does_not_exit";
        } else {
            NotificationTopic::firstOrCreate(['user_id' => $userId, 'device_token' => $deviceToken, 'platform' => $platform]);
            $result = "";
        }
        return Response::json(['error' => $result]);
    }
    /**
     * @SWG\Post(
     *   path="/notification/topic/unsubscribe",
     *   tags={"Announcement"},
     *   description="unsubscribed topics",
     *   summary="This can only be done by the logged in user.",
     *   operationId="unsubscribeTopics",
     *   @SWG\Parameter(
     *         description="device token",
     *         in="formData",
     *         name="device_token",
     *         required=true,
     *         type="string",
     *   ),
     *   @SWG\Parameter(
     *         description="topic names",
     *         in="formData",
     *         name="topic_names[]",
     *         required=true,
     *         type="array",
     *         @SWG\Items(type="string"),
     *         collectionFormat="csv"
     *   ),
     *   produces={"application/json"},
     *   @SWG\Response(
     *     response=200,
     *     description="successful operation",
     *     @SWG\Schema(
     *     type="array",
     *     @SWG\Items(
     *     type="object",
     *      @SWG\Property(property="error", type="string")
     *      ),
     *     ),
     *   ),
     * )
     */
    public function unsubscribeTopic(Request $request) {
        $userId = Auth::user()->id;
        $deviceToken = $request->get('device_token');
        if(empty($userId) || empty($deviceToken)) {
            $result = "device_token_empty_or_user_does_not_exit";
        } else {
            NotificationTopic::where('user_id', $userId)
                ->where('device_token', '=', $deviceToken)
                ->delete();
            $result = "";
        }
        Response::json(['error' => $result]);
    }
}
