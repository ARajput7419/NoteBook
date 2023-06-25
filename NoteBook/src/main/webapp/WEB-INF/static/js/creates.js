
var previewButton = document.getElementById('preview-button');
var originalButton = document.getElementById('original-button');
var markdownTextarea = document.getElementById('markdown-textarea');
var previewSection = document.getElementById('preview-section');

function convertToHtml() {
  var markdownInput = document.getElementById('markdown-textarea').value;
  var htmlOutput = document.getElementById('preview-section');

  var converter = new showdown.Converter();

 let convertedHtml  = converter.makeHtml(markdownInput);
  
  htmlOutput.innerHTML = convertedHtml;
  Prism.highlightAll();
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
             '  <pre></pre>' +
             '</div>';

    element.classList.add(""+i);
    element.insertAdjacentHTML("afterend",htmlString);

  }
  
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
              '    height: 30%;' +
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
    var inputBlock = document.querySelector(".output-block.i" + index ).lastElementChild;
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
            
            if(result.output.trim().length != 0 ){
              outputBlock.style.color="white";
              outputBlock.innerText = result.output;
            }
            else{
              outputBlock.style.color="red";
              outputBlock.innerText = result.error;
            }
          })
          .catch((error)=>{

              error.data.then((error_detail)=>{
                outputBlockParent.classList.add("show");
                outputBlock.style.color="red";
                outputBlock.innerText = result.exception;
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


  addStyle();
  


previewButton.addEventListener('click', function() {
  var markdownText = markdownTextarea.value;
  previewSection.innerHTML = markdownText;
  previewSection.style.display = 'block';
  markdownTextarea.style.display = 'none';
  convertToHtml();
  appendCodeSections();
});

originalButton.addEventListener('click', function() {
  previewSection.style.display = 'none';
  markdownTextarea.style.display = 'block';
});


function fileUpload(event){

    let csrfToken = document.getElementById("csrfToken").value;
    let file = event.target.files[0];
    let form = new FormData();
    let visibility = document.getElementById("visibility").value;
    form.append("file",file);
    let metadata = {
      method:"POST",
      body:form,
      headers: {
        'X-CSRF-Token': csrfToken
      }
      
      }
    let promise = fetch(`/api/resources/?visibility=${visibility}`,metadata);
    promise.then((response)=>{
      if(response.ok){
        toast("File Uploaded Successfully");
        return response.json();
      }
      else{
        let error = new Error("Unable to Upload File");
        error.error_reponse = response.json();
        throw error; 
      }
    })  
    .then((result)=>{

        let url = result['url'];
        let div = document.getElementsByClassName("url_field")[0];
        let url_link = document.querySelector(".url_field div a");
        div.style.display = 'block';
        url_link.innerText = url;
        url_link.href = url;
      

    })
    .catch((error)=>{
      console.log(error);
      let pr = error.error_reponse;
      pr.then((error_detail)=>{

        toast(error_detail['message']);

      });

    });

  }
document.getElementsByClassName("resource_file")[0].addEventListener('change',fileUpload);
