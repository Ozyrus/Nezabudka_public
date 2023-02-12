theme:/
    
    state: getPhoneNumber
        a: {{ selectRandomArg(answers.wantPhoneNumber) }}
        script:
            $response.replies = $response.replies || [];
            $response.replies
                 .push({
                        type:"raw",
                        body:{
                            text : "А вот и кнопка:",
                            reply_markup : {
                                "keyboard":[[{"text":"Отправить контакт","request_contact":true}]]
                                }
                          },
                        method:"sendMessage"
                    });
            
        state: gotTelegramPhoneNumber
            event: telegramSendContact
            a: Супер! Сработало!
            script:
                $client.phoneNumber = $request.rawRequest.message.contact.phone_number
                $reactions.answer(selectRandomArg(answers.yourPhoneNumberIs)) 
            go!: /newCall
            
        state: noMatch
            event: noMatch
            a: {{ selectRandomArg(answers.wantButtonPress) }}