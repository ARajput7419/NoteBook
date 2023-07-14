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

    if(start_page > 1){
        document.getElementsByClassName("p0")[0].classList.remove("disabled");
    }
    else{
        document.getElementsByClassName("p0")[0].classList.add("disabled");
    }

    if(total_pages>=start_page+3){
        document.getElementsByClassName("p4")[0].classList.remove("disabled");
    }
    else{
        document.getElementsByClassName("p4")[0].classList.add("disabled");
    }

    for( let i = 1;i<=3;i++){
        let n  =Number(document.getElementsByClassName(`pp${i}`)[0].innerText);
        if(total_pages<n){
            document.getElementsByClassName(`p${i}`)[0].classList.add("disabled");
            document.getElementsByClassName(`p${i}`)[0].classList.remove("active");
        }
        else{
            document.getElementsByClassName(`p${i}`)[0].classList.remove("disabled");
        }
    }



    if(total_pages == 0){
        toast("No Notes Found");
        let parent = document.getElementsByClassName("parent")[0];
        parent.innerHTML = "";
        return;
    }
    else{
        if(list_notes.length == 0){

            let offset = page_retrived - start_page + 1;
            let page_item = document.getElementsByClassName(`p${offset}`)[0];
            page_item.classList.remove("active");
            page_item.classList.add("disabled");
            for(let a = offset+1 ; a<=4;a++)
            {
                let next_page = document.getElementsByClassName(`p${a}`)[0];
                next_page.classList.add("disabled");
            }
            
            if(page_retrived == start_page){

                if(start_page == 1){

                    document.getElementsByClassName("p0").classList.add("disabled");
                    toast("No Notes Found");
                    let parent = document.getElementsByClassName("parent")[0];
                    parent.innerHTML = "";
                    return;

                }

                else
                {

                  document.getElementsByClassName("pp1")[0].innerText = start_page - 1;
                  document.getElementsByClassName("pp2")[0].innerText = start_page ;
                  document.getElementsByClassName("pp3")[0].innerText = start_page + 1;
                  let start_field = document.getElementById("start_page");
                  let current_field = document.getElementById("current_page");
                  start_field.value = start_page - 1 ;
                  current_field.value =  start_page - 1;
                  
                }


            }

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
                                    <a href="/notes/view/${note.id}" class="btn btn-primary">Read</a>
                                    <a onclick="changeVisibility('${note.visibility}',${note.id})" class="btn btn-success vis">Change Visibility</a>
                                </div>
                                    <p class="card-timestamp"><span>Time Stamp:</span> ${note.timestamp}</p>
                                </div>
                            </div>`;

           

            parent.insertAdjacentHTML("beforeend",cardHTML);
        
        
        } 
        
        if( page_retrived != null)
        {
            let offset = page_retrived - start_page + 1;
            let page_item = document.getElementsByClassName(`p${offset}`)[0];
            page_item.classList.add("active");
        }


        }

    }   

}


function render_resources(page_retrived,total_pages,list_resources,keyword){
    

    let start_page_field = document.getElementById("resource_start_page");
    let start_page = Number(start_page_field.value);

    if(start_page > 1){
        document.getElementsByClassName("rp0")[0].classList.remove("disabled");
    }
    else{
        document.getElementsByClassName("rp0")[0].classList.add("disabled");
    }

    if(total_pages>=start_page+3){
        document.getElementsByClassName("rp4")[0].classList.remove("disabled");
    }
    else{
        document.getElementsByClassName("rp4")[0].classList.add("disabled");
    }

    for( let i = 1;i<=3;i++){
        let n  =Number(document.getElementsByClassName(`rpp${i}`)[0].innerText);
        if(total_pages<n){
            document.getElementsByClassName(`rp${i}`)[0].classList.add("disabled");
            document.getElementsByClassName(`rp${i}`)[0].classList.remove("active");
        }
        else{
            document.getElementsByClassName(`rp${i}`)[0].classList.remove("disabled");
        }
    }



    if(total_pages == 0){
        toast("No Resources Found");
        let parent = document.getElementsByClassName("resource_parent")[0];
        parent.innerHTML = "";
        return;
    }
    else{
        if(list_resources.length == 0){

            let offset = page_retrived - start_page + 1;
            let page_item = document.getElementsByClassName(`rp${offset}`)[0];
            page_item.classList.remove("active");
            page_item.classList.add("disabled");
            for(let a = offset+1 ; a<=4;a++)
            {
                let next_page = document.getElementsByClassName(`rp${a}`)[0];
                next_page.classList.add("disabled");
            }
            
            if(page_retrived == start_page){

                if(start_page == 1){

                    document.getElementsByClassName("rp0").classList.add("disabled");
                    toast("No Resources Found");
                    let parent = document.getElementsByClassName("resource_parent")[0];
                    parent.innerHTML = "";
                    return;

                }

                else
                {

                  document.getElementsByClassName("rpp1")[0].innerText = start_page - 1;
                  document.getElementsByClassName("rpp2")[0].innerText = start_page ;
                  document.getElementsByClassName("rpp3")[0].innerText = start_page + 1;
                  let start_field = document.getElementById("resource_start_page");
                  let current_field = document.getElementById("resource_current_page");
                  start_field.value = start_page - 1 ;
                  current_field.value =  start_page - 1;
                  
                }


            }

            fetch_private_resources(page_retrived-1,keyword);

            return;
        }
        else {
    

        let parent = document.getElementsByClassName("resource_parent")[0];

        parent.innerHTML = "";

        for( let i = 0 ; i <list_resources['resources'].length;i++ )
        {

            let resource = list_resources['resources'][i];

            let cardHTML = `
            <div class="card card-spacing resource-card">
            <div class="card-body">
                <h5 class="card-title">${resource.name}</h5>
                <p class="card-text"><span>Access Link:</span> <a href="${resource.location}">${resource.location}</a></p>
                <p class="card-text"><span>Author:</span>${resource.user.name}</p>
                <p class="card-text resource_visibility_${resource.id}"><span>Visibility:</span>${resource.visibility}</p>
                <hr>
                <div class="card-buttons">
                    <a onclick="deleteResource(${resource.id})" class="btn btn-danger">Delete</a>
                    <a onclick="copyTextToClipboard('${resource.location}')" class="btn btn-primary">Copy Link</a>
                    <a onclick="changeVisibilityResources(event,'${resource.visibility}',${resource.id})" class="btn btn-success vis">Change Visibility</a>

                </div>
                <p class="card-timestamp"><span>Time stamp</span>${resource.timestamp}</p>
            </div>
        </div>             

                            `;

           

            parent.insertAdjacentHTML("beforeend",cardHTML);
        
        
        } 
        
        if( page_retrived != null)
        {
            let offset = page_retrived - start_page + 1;
            let page_item = document.getElementsByClassName(`rp${offset}`)[0];
            page_item.classList.add("active");
        }


        }

    }   

}




 async function fetch_private_notes(page_number,keyword){


   

    let new_data = fetch(`/api/notes/private/${page_number}?keyword=${keyword}`);
    let response_promise = new_data.then((response)=>{
                if(response.ok)
                return response.json();
                else 
                throw new Error("Not Able to Fetch Notes");
            });

      let render_promise = response_promise.then((res)=>{
                let total_pages = res["total_pages"];
                render_notes(page_number,total_pages,res,keyword);
                return total_pages;

            });

    return await render_promise;
  

}


async function fetch_private_resources(page_number,keyword){


   

    let new_data = fetch(`/api/resources/${page_number}?keyword=${keyword}`);
    let response_promise = new_data.then((response)=>{
                if(response.ok)
                return response.json();
                else 
                throw new Error("Not Able to Fetch Resources");
            });

      let render_promise = response_promise.then((res)=>{
                let total_pages = res["total_pages"];
                render_resources(page_number,total_pages,res,keyword);
                return total_pages;

            });

    return await render_promise;
  

}


function deleteNote(id){

    let csrfToken = document.getElementById("csrfToken").value;
    let current_page_field = document.getElementById("current_page");
    let current_page = current_page_field.value;
    let keyword_field = document.getElementById("keyword");
    let keyword = keyword_field.value;
    let metadata ={
        method:"DELETE",
        headers: {
            'X-CSRF-Token': csrfToken
          }
        
    };
    let promise = fetch(`/api/notes/${id}`,metadata);
    promise.then((response)=>{
        if(response.ok){
            toast("Note is Deleted Successfully");
            fetch_private_notes(current_page,keyword);

        }
        else{
            toast("Unable to Delete Note");
            throw new Error("Unable to Delete Note");
        }
    });

}


function deleteResource(id){

    let csrfToken = document.getElementById("csrfToken").value;
    let current_page_field = document.getElementById("resource_current_page");
    let current_page = current_page_field.value;
    let keyword_field = document.getElementById("resource_keyword");
    let keyword = keyword_field.value;
    let metadata ={
        method:"DELETE",
        headers: {
            'X-CSRF-Token': csrfToken
          }
        
    };
    let promise = fetch(`/api/resources/${id}`,metadata);
    promise.then((response)=>{
        if(response.ok){
            toast("Resource is Deleted Successfully");
            fetch_private_resources(current_page,keyword);

        }
        else{
            toast("Unable to Delete Resource");
            throw new Error("Unable to Delete Resource");
        }
    });

}



function changeVisibility(event,visibility,id){


    let target = event.target;

    let csrfToken = document.getElementById("csrfToken").value;

    let metadata = {

        method : "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json; charset=UTF-8',
            'X-CSRF-Token': csrfToken
          }

    
    };
    let promise = fetch(`/api/notes/${id}?visibility=${visibility=='Public'?'Private':'Public'}`,metadata);
    promise.then((response)=>{
        console.log(response);
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
        visibility_field.innerHTML = "<span>Visibility:</span> "+result['current_visibility'];
        let current  = result["current_visibility"];
        target.setAttribute("onclick","changeVisibility(event,"+`'${current}',${id})`);

    });
}


function changeVisibilityResources(event,visibility,id){


    let target = event.target;

    let csrfToken = document.getElementById("csrfToken").value;

    let metadata = {

        method : "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json; charset=UTF-8',
            'X-CSRF-Token': csrfToken
          }

    
    };
    let promise = fetch(`/api/resources/${id}?visibility=${visibility=='Public'?'Private':'Public'}`,metadata);
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

        let visibility_field = document.getElementsByClassName(`resource_visibility_${id}`)[0];
        visibility_field.innerHTML = "<span>Visibility:</span> "+result['current_visibility'];
        let current  = result["current_visibility"];
        target.setAttribute("onclick","changeVisibilityResources(event,"+`'${current}',${id})`);

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







function resourcePagination(offset,total_pages){


    total_pages = Number(total_pages);
    let page_item = document.getElementsByClassName(`rp${offset}`)[0];
    let current_page_field = document.getElementById("resource_current_page");
    let current_page = Number(current_page_field.value);
    let start_page_field = document.getElementById("resource_start_page");
    let start_page = Number(start_page_field.value);
    let current_offset = current_page - start_page + 1;
    let keyword_field = document.getElementById("resource_keyword");
    let keyword = keyword_field.value;
    let pp1 = document.getElementsByClassName("rpp1")[0];
    let pp2 = document.getElementsByClassName("rpp2")[0];
    let pp3 = document.getElementsByClassName("rpp3")[0];
    let p1 = document.getElementsByClassName("rp1")[0];
    let p2 = document.getElementsByClassName("rp2")[0];
    let p3 = document.getElementsByClassName("rp3")[0];
    let p0 = document.getElementsByClassName("rp0")[0];
    let p4 = document.getElementsByClassName("rp4")[0];
    let current_page_item = document.getElementsByClassName(`rp${current_page-start_page+1}`)[0];

    
     if(page_item.classList.contains("disabled") ||  current_offset==offset) return;

     else if(offset == 0) {

        let promise = fetch(`/api/resources/${start_page + offset - 1 }?keyword=${keyword}`);
        promise.then((response)=>{
            if(response.ok){            
                return response.json();
            }
            else{
                toast("Unable to Fetch Resources");
                throw new Error("Unable to Fetch Resources");
            }
        })
        .then((result)=>{

            // render 
            render_resources(null,total_pages,result,keyword);


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


        let promise = fetch(`/api/resources/${start_page + offset - 1 }?keyword=${keyword}`);
        promise.then((response)=>{
            if(response.ok){            
                return response.json();
            }
            else{
                toast("Unable to Fetch Resources");
                throw new Error("Unable to Fetch Resources");
            }
        })
        .then((result)=>{

            // render 
            render_resources(null,total_pages,result,keyword);

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

        let promise = fetch(`/api/resources/${start_page + offset - 1 }?keyword=${keyword}`);
        promise.then((response)=>{
            
            if(response.ok){            
                return response.json();
            }
            else{
                toast("Unable to Fetch Resources");
                throw new Error("Unable to Fetch Resources");
            }
        })
        .then((result)=>{

          
            // render 
            render_resources(null,total_pages,result,keyword);


            current_page_field.value = offset + start_page - 1;
            current_page_item.classList.remove("active");
            page_item.classList.add("active");

        });



     }

}




function copyTextToClipboard(text) {
    navigator.clipboard.writeText(text)
      .then((code) => {
         toast("Link Copied");
      })
      .catch((error) => {
        toast("Error While Copying Link");
      });
  }


  async function searchNotes(){

    let keyword = document.getElementById("notes_search").value;

    let total_pages = await fetch_private_notes(1,keyword);

    if(total_pages){

        let keyword_field = document.getElementById("keyword");
        let start_field = document.getElementById("start_page");
        let current_field = document.getElementById("current_page");
        keyword_field.value = keyword;
        start_field.value = 1;
        current_field.value = 1;
        for(let i = 1;i<=3;i++)
        {
                document.getElementsByClassName(`pp${i}`)[0].innerText = i;
                
                if(total_pages<i)
                { 
                    document.getElementsByClassName(`p${i}`)[0].classList.add("disabled");
                    document.getElementsByClassName(`p${i}`)[0].classList.remove("active");
            }
            else{

                document.getElementsByClassName(`p${i}`)[0].classList.remove("disabled");
                document.getElementsByClassName(`p${i}`)[0].classList.remove("active");
                
            }

        }
       
        document.getElementsByClassName("p1")[0].classList.add("active");

        document.getElementsByClassName("p0")[0].classList.add("disabled");

        if(total_pages<=3)
        document.getElementsByClassName("p4")[0].classList.add("disabled");
        else
        document.getElementsByClassName("p4")[0].classList.remove("disabled");

    }


  }



  async function searchResources(){

    let keyword = document.getElementById("resources_search").value;

    let total_pages = await fetch_private_resources(1,keyword);

    if(total_pages){

        let keyword_field = document.getElementById("resource_keyword");
        let start_field = document.getElementById("resource_start_page");
        let current_field = document.getElementById("resource_current_page");
        keyword_field.value = keyword;
        start_field.value = 1;
        current_field.value = 1;
        for(let i = 1;i<=3;i++)
        {
                document.getElementsByClassName(`rpp${i}`)[0].innerText = i;
                
                if(total_pages<i)
                { 
                    document.getElementsByClassName(`rp${i}`)[0].classList.add("disabled");
                    document.getElementsByClassName(`rp${i}`)[0].classList.remove("active");
            }
            else{

                document.getElementsByClassName(`rp${i}`)[0].classList.remove("disabled");
                document.getElementsByClassName(`rp${i}`)[0].classList.remove("active");
                
            }

        }
       
        document.getElementsByClassName("rp1")[0].classList.add("active");

        document.getElementsByClassName("rp0")[0].classList.add("disabled");

        if(total_pages<=3)
        document.getElementsByClassName("rp4")[0].classList.add("disabled");
        else
        document.getElementsByClassName("rp4")[0].classList.remove("disabled");

    }


  }





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