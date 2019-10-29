<?php

use Illuminate\Database\Seeder;
use App\Models\SurveyProperties;

class SurveyPropertiesTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
    	DB::table('survey_properties')->delete();
    	$json = File::get("public/assets/jsons/survey_properties.json");
        $data = json_decode($json, true);
        foreach ($data as $obj) {
          SurveyProperties::firstOrCreate(array(
            'id' => $obj["id"] + 1,
            'title' => $obj["title"],
            'keyword' => $obj["keyword"],
            'intro_message' => $obj["intro_message"],
            'end_message' => $obj["end_message"],
            'skip_intro' => $obj["skip_intro"],
            'index' => $obj["id"],
          ));
        }
    }
}
