
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
  let pre = document.querySelectorAll("pre");
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
             '  compiled successfully ....' +
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

function addJavascript(){
  
  var scriptElement = document.createElement("script");
scriptElement.innerHTML = `
function runCode(event,index) {
  event.preventDefault();
  var outputBlock = document.querySelector(".output-block.i" + index );
  outputBlock.classList.add("show");
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
`;
document.head.appendChild(scriptElement);
}


  addStyle();
  
  addJavascript();

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
    let progress_outer = document.getElementById("progress_outer");
    let progress_inner = document.getElementById("progress_inner");
    let file = event.target.files[0];
    let form = new FormData();
    let visibility = document.getElementById("visibility").value;
    form.append("file",file);
    let metadata = {
      method:"POST",
      body:form,
      headers: {
        'X-CSRF-Token': csrfToken
      },
      onUploadProgress: function(progressEvent) {
        if (progressEvent.lengthComputable) {
          const percent = Math.round((progressEvent.loaded / progressEvent.total) * 100);
          progress_outer.setAttribute("aria-valuenow" ,percent);
          progress_inner.innerText = percent + '%';
        }
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
