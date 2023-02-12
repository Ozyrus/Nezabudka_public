theme:/
    
    state: newCall
        script:
            $reactions.answer(selectRandomArg(answers.getDateTimeData))
        buttons:
            "Через минуту"
            "Завтра в девять утра"
            "Через час"
        
        state: correctDatetime
            intent:/датаВремя
            go!:/createRequest
            
        state: incorrectDatetime
            event: noMatch
            script:
                $reactions.answer(selectRandomArg(answers.incorrectDatetime))
            go!: /newCall
    
    state: createRequest
        script:
            $temp.offsetTime = $parseTree._date.timestamp-10800000
            $temp.DateObject = moment($temp.offseTtime)
            $session.callDateTime = $temp.DateObject.toISOString()
        a: Твое время будильника по гринвичу: {{$session.callDateTime}} Все так?
        a: Твой номер: {{$client.phoneNumber}}
        a: Твое имя: {{$client.name}}
            
        state: Yes
            intent: /да
            a: Отлично.
            script:
                $temp.formattedDate = $session.callDateTime.toString()
                $temp.formattedName = $client.name.toString()
                $temp.formattedPhone = $client.phoneNumber.toString()
                var response = makeCall($temp.formattedDate, $temp.formattedPhone, $temp.formattedName)
                if (response.isOk) {
                    $reactions.answer("Жди звонка")
                    $reactions.transition("/Start")
                } else {
                    $reactions.answer(toPrettyString(response))
                    $reactions.answer("Что-то пошло не так, странно. Попробуем заново.")
                    $reactions.transition("/newCall")
                }
        
        state: No
            event: noMatch
            go!:/newCall