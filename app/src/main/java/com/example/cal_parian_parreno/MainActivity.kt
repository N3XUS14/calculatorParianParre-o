package com.example.cal_parian_parreno

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var resultTv: TextView
    private lateinit var solutionTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTv = findViewById(R.id.result_tv)
        solutionTv = findViewById(R.id.solution_tv)

        assignId(R.id.button_c)
        assignId(R.id.button_open_bracket)
        assignId(R.id.button_close_bracket)
        assignId(R.id.button_divide)
        assignId(R.id.button_multiply)
        assignId(R.id.button_plus)
        assignId(R.id.button_minus)
        assignId(R.id.button_equals)
        assignId(R.id.button_0)
        assignId(R.id.button_1)
        assignId(R.id.button_2)
        assignId(R.id.button_3)
        assignId(R.id.button_4)
        assignId(R.id.button_5)
        assignId(R.id.button_6)
        assignId(R.id.button_7)
        assignId(R.id.button_8)
        assignId(R.id.button_9)
        assignId(R.id.button_ac)
        assignId(R.id.button_dot)
    }

    private fun assignId(id: Int) {
        val button = findViewById<MaterialButton>(id)
        button.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val button = view as MaterialButton
        val buttonText = button.text.toString()
        var dataToCalculate = solutionTv.text.toString()

        when (buttonText) {
            "AC" -> {
                solutionTv.text = ""
                resultTv.text = "0"
                return
            }
            "=" -> {
                val finalResult = calculateResult(dataToCalculate)
                resultTv.text = finalResult
                solutionTv.text = finalResult
                return
            }
            "C" -> {
                if (dataToCalculate.isNotEmpty()) {
                    dataToCalculate = dataToCalculate.dropLast(1)
                }
            }
            else -> {
                dataToCalculate += buttonText
            }
        }

        solutionTv.text = dataToCalculate
    }

    private fun calculateResult(data: String): String {
        return try {
            val expression = data.replace("×", "*").replace("÷", "/")
            val result = eval(expression)
            if (result == result.toInt().toDouble()) {
                result.toInt().toString()
            } else {
                result.toString()
            }
        } catch (e: Exception) {
            "Err"
        }
    }

    private fun eval(expression: String): Double {
        val cleanExpression = expression.replace(" ", "")
        val tokens = cleanExpression.toCharArray()
        val values = mutableListOf<Double>()
        val ops = mutableListOf<Char>()

        var i = 0
        while (i < tokens.size) {
            when {
                tokens[i].isDigit() || tokens[i] == '.' -> {
                    val sb = StringBuilder()
                    while (i < tokens.size && (tokens[i].isDigit() || tokens[i] == '.')) {
                        sb.append(tokens[i++])
                    }
                    values.add(sb.toString().toDouble())
                    i--
                }
                tokens[i] == '(' -> {
                    ops.add(tokens[i])
                }
                tokens[i] == ')' -> {
                    while (ops.isNotEmpty() && ops.last() != '(') {
                        values.add(applyOp(ops.removeAt(ops.size - 1), values.removeAt(values.size - 1), values.removeAt(values.size - 1)))
                    }
                    ops.removeAt(ops.size - 1)
                }
                tokens[i] in listOf('+', '-', '*', '/') -> {
                    while (ops.isNotEmpty() && hasPrecedence(tokens[i], ops.last())) {
                        values.add(applyOp(ops.removeAt(ops.size - 1), values.removeAt(values.size - 1), values.removeAt(values.size - 1)))
                    }
                    ops.add(tokens[i])
                }
            }
            i++
        }

        while (ops.isNotEmpty()) {
            values.add(applyOp(ops.removeAt(ops.size - 1), values.removeAt(values.size - 1), values.removeAt(values.size - 1)))
        }

        return values.last()
    }

    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        return if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            false
        } else true
    }

    private fun applyOp(op: Char, b: Double, a: Double): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> a / b
            else -> 0.0
        }
    }
}
