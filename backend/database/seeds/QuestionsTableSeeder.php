<?php

use Illuminate\Database\Seeder;
use App\Models\Question;

class QuestionsTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        DB::table('questions')->delete();
        foreach (range(0, 9) as $number) {
        	$json = File::get("public/assets/jsons/example_survey_".$number.".json");
		    $data = json_decode($json, true);
	        foreach ($data as $obj) {
	        	$question = array(
		        	'spid' => $number + 1,
		            'index' => $obj["index"],
		            'title' => $obj["question_title"],
		            'type' => $obj["question_type"],
		            'description' => $obj["description"],
		            'required' => $obj["required"],
		        );
	        	if(isset($obj['choices'])) $question['choices'] = json_encode($obj['choices']);
	        	if(isset($obj['random_choices'])) $question['random_choices'] = $obj['random_choices'];
	        	if(isset($obj['min'])) $question['min_value'] = $obj['min'];
	        	if(isset($obj['max'])) $question['max_value'] = $obj['max'];
		        Question::firstOrCreate($question);
		    }
		}        
    }
}
