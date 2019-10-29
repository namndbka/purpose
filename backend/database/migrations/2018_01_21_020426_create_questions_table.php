<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateQuestionsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('questions', function (Blueprint $table) {
            $table->increments('id');
            $table->integer('spid')->unsigned()->index('spid');
            $table->longtext('title');
            $table->enum('type', array('Audio','Checkboxes','Number','Picture','Radioboxes','String','StringMultiline','Video'))->default('String');
            $table->longtext('description')->nullable();
            $table->boolean('required')->default(0);
            $table->longtext('choices')->nullable();
            $table->boolean('random_choices')->default(0);
            $table->integer('min_value')->default(0);
            $table->integer('max_value')->default(0);
            $table->integer('index')->default(0);
            $table->boolean('hide')->default(0);
            $table->timestamps();
            $table->foreign('spid','survey_properties_ibfk_1')->references('id')->on('survey_properties')->onUpdate('RESTRICT')->onDelete('CASCADE');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('questions');
    }
}
