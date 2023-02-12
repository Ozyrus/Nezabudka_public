var systemAnswer = $reactions.answer;

function appendAnswer(arg) {
    if (typeof (arg) !== "string") {
        throw "appendAnswer argument must be a string";
    }

    var text = arg;
    text = $reactions.template(text, $jsapi.context());

    var $response = $jsapi.context().response;

    // 2. fill tts in reply
    $response.replies = $response.replies || [{
        type:"text"
    }];

    var lastReply = $response.replies[$response.replies.length - 1];
    if (lastReply.text) {
        lastReply.text = lastReply.text + " " + text;
    } else {
        lastReply.text =  text;
    }

    if ($response.answer) {
        $response.answer = $response.answer + " " + text;
    } else {
        $response.answer = text;
    }
}

function answer(arg) {
    if (arg === null || arg === undefined) {
        return;
    }
    if (typeof (arg) === "object") {
        var text = arg.value;
        text = resolveInlineDictionary(resolveVariables(resolveInlineDictionary(text)));
        if (!arg.append) {
            systemAnswer(text);
        } else {
            if (typeof (arg.value) !== "string") {
                log("function_answer: " + JSON.stringify(arg));
            }
            appendAnswer(text);
        }
        if (arg.tts) {
            tts(arg.tts);
        } else {
            tts(text);
        }
        return;
    }

    if (arguments.length > 1) {
        var idx = $reactions.random(arguments.length);
        answer(arguments[idx]);
        return;
    }

    if (typeof (arg) !== "string") {
        throw "answer argument must be a string";
    } 

    var text = arg;
    text = resolveInlineDictionary(resolveVariables(resolveInlineDictionary(text)));
    systemAnswer(text);
    tts(text); 
}

function tts(speech) {
    // 1. process substitutions
    speech = resolveInlineDictionary(speech);
    speech = resolveVariables(speech);
    speech = $reactions.template(speech, $jsapi.context());

    var $response = $jsapi.context().response;

    // 2. fill tts in reply
    var lastReply = $response.replies[$response.replies.length - 1];
    if (lastReply.tts) {
        lastReply.tts = lastReply.tts + " " + speech;
    } else {
        lastReply.tts =  speech;
    }

    // 3. fill total tts in response
    if ($response.tts) {
        $response.tts = $response.tts + " " + speech;
    } else {
        $response.tts = speech;
    }
}

function resolveInlineDictionary(string){
    var result = "";
    if (string) {
        result = string.toString().replace(/\{([^{\/]*)(\/([^}\/]*))+?\}/g, inlineDictionaryReplacer);
    }
    return result;
}

function inlineDictionaryReplacer(match, p1, p2, p3, offset, string) {
    var alternatives = match.replace(/\{|\}/g,"").split("/");
    var index = testMode() ? 0 : randomInteger(0,alternatives.length-1);
    return alternatives[index];
}

function getFullReplyText(){
    var $response = $jsapi.context().response;
    var res = '';
    
    if ($response.replies && $response.replies.length > 0) {
        for (var i=0; i<$response.replies.length; i++){
            if ($response.replies[i].text) {
                if (res != '') {
                    res += ' ';
                }
                res += $response.replies[i].text;
            }
        }        
    }

    return res;
}

$reactions.answer = answer;
