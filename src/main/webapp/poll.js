const endpoint = "http://99.245.105.87:25565/ChatAPI";

function pollandupdate(){
    fetch(endpoint)
        .then(response => response.text())
        .then(text => {
            //Current Format: username|message
            let message = text.split('|');
            if(message[0]!=="default"){
                addMessage(message[0],message[1],false);
            }
        })
        .catch(e => {console.log("Some type of error")})
}

//use to clear up polling mechanism whenever
let pollID = setInterval(pollandupdate, 1000);

