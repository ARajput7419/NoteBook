
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