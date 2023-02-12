var OPENWEATHERMAP_API_KEY = $injector.api_key;

$global.nameConverter = function($parseTree) {
    var id = $parseTree.Names[0].value;
    return Names[id].value.name;
};

$global.cityConverter = function($parseTree) {
    var id = $parseTree.Cities[0].value;
    return Cities[id].value.name;
};

function getRandomInt(min, max) {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min) + min); //The maximum is exclusive and the minimum is inclusive
}

function makeCall(datetime, phone, name) {
    return $http.post("https://app.jaicp.com/api/calls/campaign/247961767.724154946.Eib4pKvTnhqih8AdgtPF8Zpf38sdILf0X9rBd3GBsT2/addPhones", {
        timeout: 10000,
        headers: {"Content-Type": "application/json"},
        body: [{
            "phone": phone,
            "payload": {
              "name": name
            },
            "startDateTime": datetime}]
    });
}
