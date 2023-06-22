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


function render_notes(page_retrived,total_pages,list_notes,keyword){
    

    let start_page_field = document.getElementById("start_page");
    let start_page = Number(start_page_field.value);

    if(total_pages == 0){
        toast("No Notes Found");
        return;
    }
    else{
        if(list_notes.length == 0){

            let offset = page_retrived - start_page + 1;
            let page_item = document.getElementsByClassName(`p${offset}`);
            page_item.classList.remove("active");
            fetch_private_notes(page_retrived-1,keyword);
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
                                <p class="card-text visibility_${note.id}"><span>Visibility:</span> ${note.visibility}</p>
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
        
        if( page_retrived != null)
        {
            let offset = page_retrived - start_page + 1;
            let page_item = document.getElementsByClassName(`p${offset}`);
            page_item.classList.add("active");
        }


        }

    }   

}


function fetch_private_notes(page_number,keyword){

    let new_data = fetch(`/api/notes/private/${page_number}?keyword=${keyword}`);
            new_data.then((response)=>{
                if(response.ok)
                return response.json();
                else 
                throw new Error("Not Able to Fetch ");
            })
            .then((res)=>{

                let total_pages = res["total_pages"];
                let list_notes = res["notes"];
                render_notes(page_number,total_pages,list_notes,keyword);

            }).catch((error)=>{
                toast(error);
            });

}



function deleteNote(id){

    let current_page_field = document.getElementById("current_page");
    let current_page = current_page_field.value;
    let keyword_field = document.getElementById("keyword");
    let keyword = keyword_field.value;
    let metadata ={
        method:"DELETE"
    };
    let promise = fetch(`/api/notes/${id}`,metadata);
    promise.then((response)=>{
        if(response.ok){
            toast("Note is Deleted Successfully");
            fetch_private_notes(current_page,current_page,keyword);

        }
        else{
            toast("Unable to Delete Note");
            throw new Error("Unable to Delete Note");
        }
    });

}




function changeVisibility(visibility,id){
    let metadata = {

        method : "PUT",
    
    };
    let promise = fetch(`/api/notes/${id}?visibility=${visibility}`,metadata);
    promise.then((response)=>{
        if(response.ok){
            toast("Visibility Updated Successfully");
            return response.json();
        }
        else{
            toast("Not Able to Update Visibility");
            throw new Error("Not Able to Update Visibility");
        }
    })
    .then((result)=>{

        let visibility_field = document.getElementsByClassName(`visibility_${id}`)[0];
        visibility_field.innerText = result['current_visibility'];

    });
}






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
            render_notes(null,total_pages,result,keyword);


            start_page_field.value = start_page - 1;
            current_page_field.value = start_page - 1;
            pp1.innerText = start_page - 1;
            pp2.innerText = start_page;
            pp3.innerText = start_page+1;
            current_page_item.classList.remove("active");
            p1.classList.add("active");
            if(pp1.innerText>total_pages) p1.classList.add("disabled");
            else p1.classList.remove("disabled");
            if(pp2.innerText>total_pages) p2.classList.add("disabled");
            else p2.classList.remove("disabled");
            if(pp3.innerText>total_pages) p3.classList.add("disabled");
            else p3.classList.remove("disabled");
            if(p1.innerText == 1 )  {
                p0.classList.add("disabled");
            }
            if(p3.innerText < total_pages){
                p4.classList.remove("disabled");
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
            render_notes(null,total_pages,result,keyword);

            start_page_field.value = start_page + 1;
            current_page_field.value = start_page +  3;
            pp1.innerText = start_page + 1;
            pp2.innerText = start_page + 2;
            pp3.innerText = start_page+ 3;
            current_page_item.classList.remove("active");
            p3.classList.add("active");
            if(pp1.innerText>total_pages) p1.classList.add("disabled");
            else p1.classList.remove("disabled");
            if(pp2.innerText>total_pages) p2.classList.add("disabled");
            else p2.classList.remove("disabled");
            if(pp3.innerText>total_pages) p3.classList.add("disabled");
            else p3.classList.remove("disabled");
            if(p3.innerText == total_pages )  {
                p4.classList.add("disabled");
            }
            if(p1.innerText > 1 ){
                p0.classList.remove("disabled");
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
            render_notes(null,total_pages,result,keyword);


            current_page_field.value = offset + start_page - 1;
            current_page_item.classList.remove("active");
            page_item.classList.add("active");

        });



     }

}