package com.example.caluculatetraining

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //  change screen
    //  use question number
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Arrayの選択肢を作成
//        val arrayAdapter = ArrayAdapter<Int>(this, android.R.layout.simple_spinner_item)
//        arrayAdapter.add(10)
//        arrayAdapter.add(20)
//        arrayAdapter.add(30)

        val arrayAdapter = ArrayAdapter.createFromResource(this, R.array.number_Of_Question, android.R.layout.simple_spinner_item)

        //スピナーとアダプター(選択肢)を連携
        spinner.adapter = arrayAdapter

        //Function Pushing Start Button
        button.setOnClickListener {
            val numberOfQuestion: Int = spinner.selectedItem.toString().toInt()

            /*インテントの渡し先を指定する
            型推論型の記述　val intent = Intent(どこから, どこへ::class.java)*/
            val intent = Intent(this@MainActivity, CalculateActivity::class.java)

            /*渡す情報をインテントへセット
            　intent.putExtra(渡したい情報(値)を指定するKey, 渡す値)*/
            intent.putExtra("numberOfQuestion", numberOfQuestion)
            //MainActivityの継承元にあるメソッドを利用
            startActivity(intent)

        }
    }
}
