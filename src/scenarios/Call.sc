theme: /
    
    state: Call
        q: *start*
        script:
            $reactions.answer(selectRandomArg(answers.callGreeting))
        go!: /Task
        
    state: Task
        script:
            $session.firstInt = getRandomInt(6,12)
            $session.secondInt = getRandomInt(6,12)
            $session.answer = $session.firstInt*$session.secondInt
            $reactions.answer(selectRandomArg(answers.taskQuestion))
            
        state: AnswerCheck
            intent: /цифра
            script:
                $temp.match = $nlp.matchPatterns($parseTree._number,[$session.answer])
                if ($temp.match) {
                    $reactions.answer(selectRandomArg(answers.rightAnswer)),
                    $dialer.hangUp()
                } else {
                    $reactions.answer(selectRandomArg(answers.incorrectAnswer))
                    $reactions.transition("/Task")
                }
                
        state: noMatch
            event: noMatch
            a: Ладно. Давай другую.
            go!: /Task
    
    state: NoInput || noContext=true
        event: speechNotRecognized
        script:
            $session.noInputCounter = $session.noInputCounter || 0;
            $session.noInputCounter++;
    
        if: $session.noInputCounter >= 2
            a: {{ selectRandomArg(answers.callYouLater) }}
            script:
                $dialer.hangUp('(Бот повесил трубку)');
        else:
            a: {{ selectRandomArg(answers.cantHearYou) }}