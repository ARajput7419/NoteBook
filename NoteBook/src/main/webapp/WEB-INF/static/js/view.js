

function makePostRequest(url,token){


    let f = document.createElement("form");
    f.action=url;
    f.method="post";
    f.type="hidden";
    let hidden_input = document.createElement("input");
    hidden_input.type="hidden";
    hidden_input.name="_csrf";
    hidden_input.value=token;
    f.appendChild(hidden_input);
    document.body.appendChild(f);
    f.submit();

}




function addStyle(){

  let styleString = '<style>' +
              '  .code-block {' +
              '    background-color: #f1f1f1;' +
              '    padding: 10px;' +
              '  }' +
              '  .input-block,' +
              '  .output-block {' +
              '    display: none;' +
              '    margin-top: 2%;' +
              '  }' +
              '  .input-block.show,' +
              '  .output-block.show {' +
              '    display: block;' +
        
              '  }' +

              '  .output-block {' +
              '    background-color: black;' +
              '    color: white;' +
              '    padding: 10px;' +
              '    font-weight: bold;' +
              '    margin-top: 10px;' +
              '    overflow-x: scroll;' +
              '    overflow-y: scroll;' +
              '    height: 45%;' +
              '  }' +
              '  .run-button {' +
              '    margin-top: 10px;' +
              '  }' +
              '  .input-button {' +
              '    margin-left: 10%;' +
              '  }' +
              '  .code-labels {' +
              '    font-weight: bold;' +
              '  }' +
              '</style>';

 document.head.insertAdjacentHTML("beforeend",styleString);

}



  function appendCodeSections(){
    let pre = document.querySelectorAll('pre[class^="language"]');
    for( let i = 0;  i < pre.length; i++){
  
      let element = pre[i];
      
      var htmlString = '<button class="btn btn-primary run-button i'+i+' "  onclick="runCode(event,'+i+')">Run</button>' +
               '<button class="btn btn-primary run-button input-button" onclick="showInput(event,'+i+')">Input</button>' +
               '<div class="input-block i' +i+'">' +
               '  <label class="code-labels" for="inputValue">Input:</label>' +
               '  <textarea rows = "4" style="background-color: black; color:white;font-weight: bold;" class="form-control"></textarea>' +
               '</div>' +
               '<div class="output-block i'+ i +'" id="outputBlock">' +
               '  <label class="code-labels" for="output">Output:</label><br>' +
               '  <pre style="overflow:visible;"</pre>' +
               '</div>';
  
      element.classList.add(""+i);
      element.insertAdjacentHTML("afterend",htmlString);
  
    }
    
  }


 let languages = ["cpp","java","python"];
  
 class CustomError extends Error {
  constructor(message, data) {
    super(message);
    this.data = data;
  }
}

  


function runCode(event,index) {
  event.preventDefault();
  let csrfToken = document.getElementById("csrfToken").value;
  var outputBlock = document.querySelector(".output-block.i" + index ).lastElementChild;
  var outputBlockParent = document.querySelector(".output-block.i" + index );
  var inputBlock = document.querySelector(".input-block.i" + index ).lastElementChild;
  let pre = document.querySelector(`pre[class^="language"][class~="${index}"]`);
  let code_field = pre.firstElementChild;
  if(code_field!= null){
    let code = code_field.innerText;
    let input = inputBlock.value;
    for(let lang of code_field.classList){
      if(lang == languages[0] || lang == languages[1] || lang == languages[2]){
        let data = {
          code:code,
          lang:lang,
          input:input
        }
        let meta = {
          method: "post",
          headers:{'Content-Type': 'application/json',
          'X-CSRF-Token': csrfToken

        }
          ,
        body: JSON.stringify(data)
        };
        let request = fetch("/api/execution/exec",meta);
        request.then((response)=>{
          
          if(response.ok) return response.json();
          else{
            throw new CustomError(null,response.json());
          }

        })
        .then((result)=>{
          console.log(result);
          outputBlockParent.classList.add("show");
          outputBlock.style.color="white";
          outputBlock.innerText = result.output;
          
        })
        .catch((error)=>{

            error.data.then((error_detail)=>{
              outputBlockParent.classList.add("show");
              outputBlock.style.color="red";
              outputBlock.innerText = error_detail.exception;
            });
        });


      }
    }
  }
}


  
  function showInput(event,index) {
    event.preventDefault();
    var inputBlock = document.querySelector(".input-block.i"+index);
    if (inputBlock.classList.contains("show")) {
      inputBlock.classList.remove("show");
    } else {
      inputBlock.classList.add("show");
    }
  }

 
   
  