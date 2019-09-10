package com.example.caluculatetraining

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_calculate.*
import kotlinx.android.synthetic.main.activity_calculate.view.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random

class CalculateActivity : AppCompatActivity(), View.OnClickListener {

    //問題数
    var numberOfQuestion: Int = 0
    //残り問題数
    var numberOfRemain: Int = 0
    //正解数
    var numberOfCrrect: Int = 0
    //効果音の変数
    lateinit var soundPool: SoundPool

    //サウンドID
    var intSoundId_Correct: Int = 0
    var intSoundId_Incorrect: Int = 0

    //タイマーの変数
    lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate)


//     テスト画面を開いたら
//     Main画面から渡された問題数を表示する
      val bundle = intent.extras
      numberOfQuestion = bundle.getInt("numberOfQuestion")
      remain.text = numberOfQuestion.toString()
        numberOfRemain = numberOfQuestion
      numberOfCrrect = 0

//    Todo 「答え合わせ」ボタンの押下後
//    答え合わせボタンの処理 回答欄が空白などの数字以外が入力されたら反応しないようにする
      answerCheck.setOnClickListener {
          if (answer.text.toString() != "" && answer.text.toString() != "-"){
              answerCheck()
          }
      }

//     「もどる」ボタンの押下後
      back.setOnClickListener {
          finish()
      }

      //電卓ボタンひとつひとつの処理
      button0.setOnClickListener(this)
      button1.setOnClickListener(this)
      button2.setOnClickListener(this)
      button3.setOnClickListener(this)
      button4.setOnClickListener(this)
      button5.setOnClickListener(this)
      button6.setOnClickListener(this)
      button7.setOnClickListener(this)
      button8.setOnClickListener(this)
      button9.setOnClickListener(this)
      buttonclear.setOnClickListener(this)
      buttonminus.setOnClickListener(this)


        //一問目を出すときの処理
        question()

    }

    override fun onResume() {
        super.onResume()

        //soundPoolの準備
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            SoundPool.Builder().setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build())
                .setMaxStreams(1)
                .build()
        }else {
            SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        }

        //効果音ファイルをメモリにロード
        intSoundId_Correct = soundPool.load(this,R.raw.correct1,1)
        intSoundId_Incorrect = soundPool.load(this, R.raw.incorrect1,1)

        //タイマーの変数インスタンス化
        timer = Timer()


    }

    override fun onPause() {
        super.onPause()
        //メモリの効果音を片付け
        soundPool.release()
        timer.cancel()
    }



    //問題を出す処理のめっそど
    // 問題が出されたら(questionMethod)
    private fun question() {
        //戻るボタンを使えなくする
        back.isEnabled = false
        //答え合わせボタンと電卓ボタンを使えるようにする
        answerCheck.isEnabled = true
        button0.isEnabled = true
        button1.isEnabled = true
        button2.isEnabled = true
        button3.isEnabled = true
        button4.isEnabled = true
        button5.isEnabled = true
        button6.isEnabled = true
        button7.isEnabled = true
        button8.isEnabled = true
        button9.isEnabled = true
        buttonclear.isEnabled = true
        buttonminus.isEnabled = true

        //問題の2つの数字を1～100までランダムに設定して表示
        val random = Random
        val intQuestionLeft = random.nextInt(100) + 1
        val intQuestionRight = random.nextInt(100) + 1
        numleft.text = intQuestionLeft.toString()
        numright.text = intQuestionRight.toString()
        //計算方法を＋とーからランダムで設定して表示
        when(random.nextInt(2)) {
            0 -> operater.text = "+"
            1 -> operater.text = "-"
        }


        //前の問題で入力した自分の答えを削除
        answer.text = ""
        //〇や×の画像を非表示にする
        trueOrFalse.visibility = View.INVISIBLE
    }

    //    答え合わせ処理をするメソッド
    private fun answerCheck() {

        //「もどる」「答え合わせ」「電卓」ボタンを使えないようにする
        back.isEnabled = false
        answerCheck.isEnabled = false
        button0.isEnabled = false
        button1.isEnabled = false
        button2.isEnabled = false
        button3.isEnabled = false
        button4.isEnabled = false
        button5.isEnabled = false
        button6.isEnabled = false
        button7.isEnabled = false
        button8.isEnabled = false
        button9.isEnabled = false
        buttonclear.isEnabled = false
        buttonminus.isEnabled = false

        //残り問題数を一つ減らす
        numberOfRemain -= 1
        remain.text = numberOfRemain.toString()

        //●×画像の表示
        trueOrFalse.visibility = View.VISIBLE

        //自分の入力した答えと答えを比較

        //自分の答え
        val intMyAnswer: Int = answer.text.toString().toInt()
        //正解の答え
        val intCorrectAnswer: Int =
            if (operater.text == "+"){
                numleft.text.toString().toInt() + numright.text.toString().toInt()
            } else {
                numleft.text.toString().toInt() - numright.text.toString().toInt()
            }

        //二つの答えの比較
        //正解の時
        if ( intMyAnswer == intCorrectAnswer){
            numberOfCrrect += 1
            correct.text = numberOfCrrect.toString()
            trueOrFalse.setImageResource(R.drawable.maru)
            soundPool.play(intSoundId_Correct, 1.0f, 1.0f, 0, 0, 1.0f)
        }
        //不正解の時
        else {
            trueOrFalse.setImageResource(R.drawable.batu)
            soundPool.play(intSoundId_Incorrect, 1.0f, 1.0f, 0, 0, 1.0f)
        }

        //正解率の計算と表示
        val intPoint: Int = (numberOfCrrect.toDouble() / (numberOfQuestion - numberOfRemain).toDouble() * 100).toInt()
        point.text = intPoint.toString()

        //残り問題数に合わせた処理
        if(numberOfRemain == 0){
            back.isEnabled = true
            answerCheck.isEnabled = false
            message.text = "テスト終了"
        }//問題が残っていたら、次の問題へ移る
        else{
            //runOnUniThreadのおかげでサブスレッドが使える
            timer.schedule(1000, {runOnUiThread { question()} })
        }

    }
    //    電卓ボタンが押下された際の処理
//     電卓ボタンの押下後
    override fun onClick(v: View?) {

    //電卓ボタンを押すたびに一文字ずつ表示

    //vの方がViewだったのを、Button型に変換している
        val button: Button = v as Button

        when(v.id){
            //clearの押下
            R.id.buttonclear -> answer.text = ""

            //minusの押下
            R.id.buttonminus -> if (answer.text.toString() == "")
                                answer.text = "-"
            //0の押下
             R.id.button0 -> if (answer.text.toString() != "0" && answer.text.toString() != "-")
                                answer.append(button.text)

        else -> if(answer.text.toString() == "0")
                answer.text = button.text
                else answer.append(button.text)
        }
    }
}



