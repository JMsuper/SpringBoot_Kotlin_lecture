package com.example.todopractice.validator

import com.example.todopractice.annotation.StringFormatDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class StringFormatDateTimeValidator : ConstraintValidator<StringFormatDateTime,String>{
    private var pattern:String?=null

    override fun initialize(constraintAnnotation: StringFormatDateTime?) {
        super.initialize(constraintAnnotation)
        this.pattern = constraintAnnotation?.pattern
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return try {
            LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern))
            true
        }   catch (e : Exception){
            false
        }

    }
}