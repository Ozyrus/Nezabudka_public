require: answers/answers.yaml
    var = answers
    
require: functions/functions.js

require: functions/answer.js
    
require: scenarios/getName.sc

require: scenarios/requestCall.sc

require: scenarios/getPhoneNumber.sc

require: scenarios/newCall.sc

require: scenarios/Call.sc

require: scenarios/getLocation.sc

require: dateTime/dateTime.sc
    module = sys.zb-common
    
require: common.js
    module = sys.zb-common

require: namesRu/names-ru.csv
    module = sys.zb-common
    name = Names
    var = Names
    
require: city/cities-ru.csv
    module = sys.zb-common
    name = Cities
    var = Cities
    
patterns:
    $name = $entity<Names> || converter = nameConverter
    $city = $entity<Cities> || converter = cityConverter

theme: /
    
    state: Reset
        q!: $regex</reset>
        script:
            $session = {}
            $client = {}
        go!: /Start
    
    state: Start
        q!: $regex</start>
        script:
            if ($request.channelType == 'telegram' || $request.channelType == 'chatwidget'){
                $reactions.transition("/Telegram")
            } else {
                $reactions.transition("/Call")
            }
    
    state: Telegram
        if: $client.name
            a: {{ selectRandomArg(answers.startSecondTime) }}
            go!: /Abilities
        else:
            go!: /Start first time
            
    state: Abilities
        intent!: /умения
        random:
            script:
                $reactions.answer(selectRandomArg(answers.canRequestCall))
        state: Yes
            intent: /да
            go!: /requestCall
                
        state: NoMatch
            event: noMatch
            a: {{ selectRandomArg(answers.askWhenYouWant) }}

    state: Echo
        event!: noMatch
        a: Вы сказали: {{$parseTree.text}}
