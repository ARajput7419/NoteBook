
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

            fetch_public_notes(page_retrived-1,keyword);

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
                
                    <a href="/notes/view/${note.id}" class="btn btn-primary">Read</a>

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


async function fetch_public_notes(page_number,keyword){


   

    let new_data = fetch(`/api/notes/public/${page_number}?keyword=${keyword}`);
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

        let promise = fetch(`/api/notes/public/${start_page + offset - 1 }?keyword=${keyword}`);
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


        let promise = fetch(`/api/notes/public/${start_page + offset - 1 }?keyword=${keyword}`);
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

        let promise = fetch(`/api/notes/public/${start_page + offset - 1 }?keyword=${keyword}`);
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









  async function searchNotes(){

    let keyword = document.getElementById("notes_search").value;

    let total_pages = await fetch_public_notes(1,keyword);

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





