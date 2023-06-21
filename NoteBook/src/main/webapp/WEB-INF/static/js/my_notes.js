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

        parent.innerHTML = "";

        for( let i = 0 ; i <list_notes['notes'].length;i++ )
        {

            let note = list_notes['notes'][i];

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

            console.log(cardHTML);

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


    total_pages = Number(total_pages);
    let page_item = document.getElementsByClassName(`p${offset}`)[0];
    let current_page_field = document.getElementById("current_page");
    let current_page = Number(current_page_field.value);
    let start_page_field = document.getElementById("start_page");
    let start_page = Number(start_page_field.value);
    let current_offset = current_page - start_page + 1;
    let keyword_field = document.getElementById("keyword");
    let keyword = keyword_field.value;
    let pp1 = document.getElementsByClassName("pp1")[0];
    let pp2 = document.getElementsByClassName("pp2")[0];
    let pp3 = document.getElementsByClassName("pp3")[0];
    let p1 = document.getElementsByClassName("p1")[0];
    let p2 = document.getElementsByClassName("p2")[0];
    let p3 = document.getElementsByClassName("p3")[0];
    let p0 = document.getElementsByClassName("p0")[0];
    let p4 = document.getElementsByClassName("p4")[0];
    let current_page_item = document.getElementsByClassName(`p${current_page-start_page+1}`)[0];

    
     if(page_item.classList.contains("disabled") ||  current_offset==offset) return;

     else if(offset == 0) {

        let promise = fetch(`/api/notes/private/${start_page + offset - 1 }?keyword=${keyword}`);
        promise.then((response)=>{
            if(response.ok){            
                return response.json();
            }
            else{
                toast("Unable to Fetch Notes");
                throw new Error("Unable to Fetch Notes");
            }
        })
        .then((result)=>{

            // render 
            render_notes(total_pages,result,keyword);


            start_page_field.value = start_page - 1;
            current_page_field.value = start_page - 1;
            pp1.value = start_page - 1;
            pp2.value = start_page;
            pp3.value = start_page+1;
            current_page_item.classList.remove("active");
            p1.classList.add("active");
            if(pp1.value>total_pages) p1.classList.add("disabled");
            else p1.classList.remove("disabled");
            if(pp2.value>total_pages) p2.classList.add("disabled");
            else p2.classList.remove("disabled");
            if(pp3.value>total_pages) p3.classList.add("disabled");
            else p3.classList.remove("disabled");
            if(p1.value == 1 )  {
                p0.classList.add("disabled");
            }

        });
    
     }
     else if(offset == 4){


        let promise = fetch(`/api/notes/private/${start_page + offset - 1 }?keyword=${keyword}`);
        promise.then((response)=>{
            if(response.ok){            
                return response.json();
            }
            else{
                toast("Unable to Fetch Notes");
                throw new Error("Unable to Fetch Notes");
            }
        })
        .then((result)=>{

            // render 
            render_notes(total_pages,result,keyword);

            start_page_field.value = start_page + 1;
            current_page_field.value = start_page +  3;
            pp1.value = start_page + 1;
            pp2.value = start_page + 2;
            pp3.value = start_page+ + 3;
            current_page_item.classList.remove("active");
            p3.classList.add("active");
            if(pp1.value>total_pages) p1.classList.add("disabled");
            else p1.classList.remove("disabled");
            if(pp2.value>total_pages) p2.classList.add("disabled");
            else p2.classList.remove("disabled");
            if(pp3.value>total_pages) p3.classList.add("disabled");
            else p3.classList.remove("disabled");
            if(p3.value == total_pages )  {
                p4.classList.add("disabled");
            }

        });





     }
     else {

        let promise = fetch(`/api/notes/private/${start_page + offset - 1 }?keyword=${keyword}`);
        promise.then((response)=>{
            
            if(response.ok){            
                return response.json();
            }
            else{
                toast("Unable to Fetch Notes");
                throw new Error("Unable to Fetch Notes");
            }
        })
        .then((result)=>{

          
            // render 
            render_notes(total_pages,result,keyword);


            current_page_field.value = offset + start_page - 1;
            current_page_item.classList.remove("active");
            page_item.classList.add("active");

        });



     }

}