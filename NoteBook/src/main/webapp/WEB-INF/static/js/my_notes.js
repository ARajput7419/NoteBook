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
    
            // yet to complete 

        }

    }   

}


function fetch_private_notes(page_number,keyword){

    let new_data = fetch(`/api/notes/private/${page_number}?keyword=${keyword}`);
            new_data.then((response)=>{
                if(response.ok)
                return response.json();
                else 
                throw new Error("Not Able to fetch ");
            })
            .then((res)=>{

                let total_pages = res["total_pages"];
                let list_notes = res["notes"];
                render_notes(total_pages,list_notes,keyword);

            }).catch((error)=>{
                toast(error);
            });

}


function ondeletNoteClick(id,page_number){

    let keyword = document.getElementById("note_keyword");
    let request = fetch(`/api/notes/${id}`);
    request.then((response)=>{

        if(response.ok){
            fetch_private_notes(page_number,keyword);
        }

        return response.json();
    })
    .then((value)=>{
        toast(value["message"]);
    });
}


function change_visibility(visibility){
    
}