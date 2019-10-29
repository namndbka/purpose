<?php
namespace App\Models;
use Eloquent as Model;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: 10/3/16
 * Time: 9:20 AM
 */

class BaseModel extends Model {
    public function manyThroughMany($related, $through, $firstKey, $secondKey, $pivotKey)
    {
        $model = new $related;
        $table = $model->getTable();
        $throughModel = new $through;
        $pivot = $throughModel->getTable();

        return $model
            ->join($pivot, $pivot . '.' . $pivotKey, '=', $table . '.' . $secondKey)
            ->select($table . '.*')
            ->where($pivot . '.' . $firstKey, '=', $this->id);
    }
}