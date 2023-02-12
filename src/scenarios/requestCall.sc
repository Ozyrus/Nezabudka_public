theme: /
    
    state: requestCall
        intent!: /будильник
        script:
            if ($client.phoneNumber) {
                $reactions.transition("/newCall")
            } else {
                $reactions.transition("/getPhoneNumber")
            }