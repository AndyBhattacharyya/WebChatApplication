const endpoint = "http://localhost:8080/ChatAPI";

function beginSSE(){
    let evtSource = new EventSource(endpoint);
    evtSource.onmessage = (event) => {console.log(event.data)}
}
beginSSE();


/*
function pollandupdate(){
    fetch(endpoint)
        .then(response => {
            //Check if API returns content-length=0
            let content_length = response.headers.get("Content-Length");
            if (response.redirected){
                window.location.href=response.url;
                clearInterval(pollID);
                throw new Error("Invalid Cookie");
            }
            else if(content_length==0){
                throw new Error("No message to update");
            }
            else{
                return response.json();
            }
        })
        .then(text => {
            //Current Format: username|message
            let userOfMessage = text.user;
            let message = text.message;
            addMessage(userOfMessage,message,false);
        })
        .catch(e => e)
}

//use to clear up polling mechanism whenever
let pollID = setInterval(pollandupdate, 1000);
 */
