package com.muzo.simplecalculate

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.muzo.simplecalculate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var canAddDecimal = true
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            binding.workingsTv.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }

    }

    fun numberAction(view: View) {
        if (view is Button) {

            if (view.text == ".") {
                if (canAddDecimal) binding.workingsTv.append(view.text)
                canAddDecimal = false
            } else

                binding.workingsTv.append(view.text)
            canAddOperation = true
        }
    }

    fun allClearAction(view: View) {

        binding.workingsTv.text = ""
        binding.resultsTv.text = ""

    }

    fun backSpaceAction(view: View) {
        val length = binding.workingsTv.length()
        if (length > 0) binding.workingsTv.text = binding.workingsTv.text.subSequence(0, length - 1)
    }

    fun equalsAction(view: View) {
        binding.resultsTv.text = calculateResults()
    }

    private fun calculateResults(): String {
        val digitOperators = digitOperators()
        if (digitOperators.isEmpty()) return ""

        val timeDivision = timedDivisonCalculate(digitOperators)
        if (timeDivision.isEmpty()) return ""
        val result = addSubstractCalculate(timeDivision)

        return result.toString()
    }

    private fun addSubstractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for(i in passedList .indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator=passedList[i]
                val nextDigit=passedList[i+1] as Float
                if (operator=='+')
                    result +=nextDigit
                if (operator=='-')
                    result -=nextDigit
            }
        }
        return result
    }

    private fun timedDivisonCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/')) {

            list = calcTimesDiv(list)
        }
        return list

    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {

        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size
        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float
                when (operator) {
                    'x' -> {
                        newList.add(prevDigit.times(nextDigit))
                        restartIndex = i + 1
                    }

                    '/' -> {
                        newList.add(prevDigit.div(nextDigit))
                        restartIndex = i + 1
                    }

                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if (i > restartIndex) newList.add(passedList[i])
        }
        return newList
    }

    private fun digitOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""

        for (character in binding.workingsTv.text) {
            if (character.isDigit() || character == '.') currentDigit += character
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if (currentDigit != "") list.add(currentDigit.toFloat())


        return list

    }
}