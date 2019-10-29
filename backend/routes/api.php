<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::group(['prefix' => 'v1', 'namespace' => 'Api', 'middleware' => 'access_api:api'], function(){
	Route::post('subcribe_email', 'SurveyController@subcribeEmail');
	Route::post("answer_survey", 'SurveyController@answerSurvey');
	Route::post("upload_video", 'SurveyController@uploadVideo');
	// notification
	Route::post('notification/topics', 'SurveyController@getSubscribedTopic');
    Route::post('notification/topic/subscribe', 'SurveyController@subscribeTopic');
    Route::post('notification/topic/unsubscribe', 'SurveyController@unsubscribeTopic');
	
});

