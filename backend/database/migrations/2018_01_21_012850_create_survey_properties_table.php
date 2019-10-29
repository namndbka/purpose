<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateSurveyPropertiesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('survey_properties', function (Blueprint $table) {
            $table->increments('id');
            $table->string('title');
            $table->string('keyword')->nullable();
            $table->longtext('intro_message')->nullable();
            $table->longtext('end_message')->nullable();
            $table->boolean('skip_intro')->default(0);
            $table->integer('index')->unsigned()->index('index');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('survey_properties');
    }
}
