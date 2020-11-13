
<h2>ServicePro Rest API</h2>

This <b>Rest api</b> serves as a Backend for a mobile application that you can find it in this repo: <br/>https://github.com/Aouatef-Djeghri/Android_Java_Application.git )<br/>

--------------------
It was implemented using :<br/>

jersey 2.29<br/>
Hibentae verion 3.4<br/>
Jackson JSON 2.10.5<br/>
Maven<br/>
MySQL<br/>


--------------------
<h2>ServicePro Rest Api endPoints :</h2>


LoginResources : <br/>
POST 	/login<br/>

--------------------

CategorieResources:<br/>

GET	/categories<br/>
GET	/categories/available<br/>
GET	/categories/unvailable<br/>
GET 	/categories{id}<br/>
GET 	/categories/{categoryId}/services<br/>
GET 	/categories/{categoryId}/availableServices<br/>
GET 	/categories/{categoryId}/unavailableServices<br/>
POST	/categories/create<br/>
PUT     /categories/update<br/>
DELETE	/categories/delete/{id}<br/>

--------------------

ServiceResources:<br/>

GET 	/services<br/>
GET 	/services/{id}<br/>
GET 	/services/available<br/>
GET 	/services/unavailable<br/>
GET 	/services/user/{userId}<br/>
PUT		/services/user/{userId}/update<br/>
POST	/services/create<br/>
PUT	    /services/update<br/>
DELETE 	/services/delete/{id}<br/>

--------------------

UserResources:<br/>

GET 	/users<br/>
GET 	/users/{id}<br/>
POST	/users/createUser<br/>
POST	/users/updateUser<br/>
POST	/users/updatePassword<br/>
POST	/users/findArtisans<br/>

-------------------

WorkResources:<br/>

GET 	/works/{role}/{userId}<br/>
GET 	/works/{role}/{userId}/{status}<br/>
GET 	/works/posts/{userId}<br/>
GET		/works/posts/recommendation/user/{userId}<br/>
GET		/works/posts/recommendation/category/{categoryId}/{userId}<br/>
GET		/works/posts/recommendation/service/{serviceId}/{userId}<br/>
POST	/works/createWorkPost<br/>
POST	/works/createWorkProposal<br/>
POST	/works/acceptWorkProposal<br/>
POST	/works/declineWorkProposal<br/>
POST	/works/finishWork<br/>

-------------------

UploadResources:<br/>

POST 	/upload/image<br/>
POST 	/upload/images<br/>
POST 	/upload/delete<br/>


-------------------

ApplicationResources:<br/>

GET 	/applications <br/>
GET 	/applications/{id}<br/>
POST 	/applications/createApplication<br/>

-------------------

ReviewResources:<br/>

<h4>get the list of all the reviews</h4>
GET 	/reviews
<h4>get review by id</h4>
GET 	/reviews/{id}
<h4>get work reviews by work id</h4>
GET 	/reviews/work/{workId}
<h4>to get this list of all the reviews that the user has made about others as a client or as an artisan </h4>
GET 	/reviews/user/{userId}/reviewer
<h4>to get this list of all the reviews that the others have made about this user</h4>
GET 	/reviews/user/{userId}/reviewee
<h4>to get this list of reviews made about the user as a client</h4>
GET 	/reviews/user/{clientId}/client/reviewee
<h4>to get this list of reviews made about the user as a artisan</h4>
GET 	/reviews/user/{artisanId}/artisan/reviewee
<h4>to get this list of reviews that the user made as a client</h4>
GET 	/reviews/user/{clientId}/client/reviewer
<h4>to get this list of reviews that the user made as an artisan</h4>
GET 	/reviews/user/{artisanId}/artisan/reviewer

<h4>add review</h4>
POST 	/reviews/create
<h4>update review</h4>
PUT 	/reviews/update
<h4>delete review</h4>
DELETE 	/reviews/delete/{id}<br/>



