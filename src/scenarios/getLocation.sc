theme: /
    
    state: getLocation
        intent: /часовой пояс
        a: {{ selectRandomArg(answers.needLocation) }}
        
        state: gotLocation
            q: * $city *
            script:
                $client.city = $parseTree._city
            a: Ваш часовой пояс: {{ $client.timezone }} 
            a: Время на сервере: {{ currentDate().unix() }}
            go!: /newCall
            
        state: notLocation
            event: noMatch
            a: Не похоже на город.
            go!: /getLocation