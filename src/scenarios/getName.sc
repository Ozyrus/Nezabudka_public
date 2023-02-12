theme:/
    
    state: ChangeName
        q!: меня зовут $name
        q!: * $name *
        script:
            $client.name = $parseTree._name
        a: {{ selectRandomArg(answers.gotNewName) }}
        go!: /Abilities

    state: Start first time
        a: {{ selectRandomArg(answers.startFirstTime) }}
        
        state: GetName|| modal=true
            q: * $name *
            script:
                $client.name = capitalize($parseTree._name)
            a: {{ selectRandomArg(answers.goodToKnowYou) }}
            go!: /Abilities
        
        state: Fallback name
            event: noMatch
            a: {{ selectRandomArg(answers.notAName) }}
            go!: /Start first time