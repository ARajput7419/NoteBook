const switchButton = document.querySelector('.switch-button');
const notesButton = document.querySelector('.notes_button');
const noteCards = document.querySelector('.notes-container');
const resourceCards = document.querySelector('.resource-container');


noteCards.style.display="block";
resourceCards.style.display="none";

switchButton.addEventListener('click', () => {
    noteCards.style.display="none";
    resourceCards.style.display="block";
});

notesButton.addEventListener('click', () => {
    resourceCards.style.display="none";
    noteCards.style.display="block";
});


function render_notes(total_pages,list_notes,keyword){
    if(total_pages == 0){
        toast("No Notes Found");
        return;
    }
    else{
        if(list_notes.length == 0){
            fetch_private_notes(page_number,keyword);
            return;
        }
        else {
    

        let parent = document.getElementsByClassName("parent")[0];

        parent.innserHTML = "";

        for( let i = 0 ; i <list_notes.length;i++ )
        {

            let note = list_notes[i];

            let cardHTML = `
                            
                            <div class="card card-spacing notes-card">
                                <div class="card-body">
                                <h5 class="card-title">${note.name}</h5>
                                <p class="card-text"><span>Visibility:</span> ${note.visibility}</p>
                                <p class="card-text"><span>Author:</span> ${note.user.name}</p>
                                <hr>
                                <div class="card-buttons">
                                    <a onclick="deleteNote('${note.id}')" class="btn btn-danger">Delete</a>
                                    <a href="/view/${note.id}" class="btn btn-primary">Read</a>
                                    <a href="changeVisibility('${note.visibility}',${note.id})" class="btn btn-success vis">Change Visibility</a>
                                </div>
                                    <p class="card-timestamp"><span>Time Stamp:</span> ${note.timestamp}</p>
                                </div>
                            </div>`;

            parent.insertAdjacentHTML("beforeend",cardHTML);
        
        
        }   

        }

    }   

}


// function fetch_private_notes(page_number,keyword){

//     let new_data = fetch(`/api/notes/private/${page_number}?keyword=${keyword}`);
//             new_data.then((response)=>{
//                 if(response.ok)
//                 return response.json();
//                 else 
//                 throw new Error("Not Able to fetch ");
//             })
//             .then((res)=>{

//                 let total_pages = res["total_pages"];
//                 let list_notes = res["notes"];
//                 render_notes(total_pages,list_notes,keyword);

//             }).catch((error)=>{
//                 toast(error);
//             });

// }


// function ondeletNoteClick(id,page_number){

//     let keyword = document.getElementById("note_keyword");
//     let request = fetch(`/api/notes/${id}`);
//     request.then((response)=>{

//         if(response.ok){
//             fetch_private_notes(page_number,keyword);
//         }

//         return response.json();
//     })
//     .then((value)=>{
//         toast(value["message"]);
//     });
// }


// function change_visibility(visibility){
    
// }


function pagination(offset,total_pages){


    let current_page_field = document.getElementById("current_page");
    let start_page_field = document.getElementById("start_page");
    let keyword_field = document.getElementById("keyword");
    let keyword = keyword_field.value;
    let current_page = current_page_field.value;
    let start_page = start_page_field.value;
    let page_item = document.getElementsByClassName(`p${offset}`)[0];
    let current_page_item = document.getElementsByClassName(`p${current_page-start_page+1}`)[0];
    
     if( offset>0 && offset < 4 ){
     
        if(page_item.classList.contains("disable")){
                return;
        }   
        else{
                
            let p = fetch(`/api/notes/private/${offset-1+start_page}?keyword=${keyword}`);
            p.then((response)=>{
                if(response.ok){
                    current_page_field.value = offset-1+start_page;
                    current_page_item.classList.remove("active");
                    page_item.classList.add("active");
                    return response.json();
                }
                else{
                    toast("Unable to Fetch Notes");
                }
            })
            .then((res)=>{

                // render notes 

            }); 
            


     }
    }
    else if(offset == 0){

    }
    else{

    }



}




