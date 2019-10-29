<?php
/**
 * Created by PhpStorm.
 * User: namnd
 * Date: 21/01/2018
 * Time: 10:52 AM
 */

namespace App\Models;

/**
 * @SWG\Definition(type="object", @SWG\Xml(name="Question"))
 */
class Question extends BaseModel {

    protected $fillable = ['title', 'type'];

    protected $casts = [
        'created_at' => 'timestamp',
        'updated_at' => 'timestamp'
    ];
}