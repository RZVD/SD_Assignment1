import {Component, model, Inject} from '@angular/core';
import {MatDialogActions, MatDialogContent, MatDialogTitle, MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatCardModule} from '@angular/material/card';
import {NgFor} from '@angular/common';
import {MatInputModule} from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { publishFacade } from '@angular/compiler';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import { NgIf } from '@angular/common';
import { Router } from '@angular/router';



export type Question = {
    title: string,
    text: string,
    userId: string,
    tags: string,
    picturePath: string
}
@Component({
    selector: 'app-questions',
    standalone: true,
    imports: [MatCardModule, NgFor,NgIf, MatInputModule, FormsModule, MatFormFieldModule, MatDialogTitle, MatDialogActions, MatDialogContent,],
    templateUrl: './questions.component.html',
    styleUrl: './questions.component.scss'
})

export class QuestionsComponent {
    private baseUrl: string = "http://localhost:8080/posts";
    private selectedTags: string[] = [];
    public userId: string | null | undefined;
    questions: any[] = []
    
    newQuestion: Question = { title: '', text: '', userId: '', tags: "", picturePath: ""};
    constructor(
        private router: Router
    ){}

    ngOnInit(): void {
        this.userId=localStorage.getItem("userId")
        if (!this.userId) {
          this.router.navigate(['/login']); // Redirect to login page if not logged in
        }
        this.fetchQuestions();
    }


    fetchQuestions() {
        fetch(`${this.baseUrl}/getAllQuestions`, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
            }
        })
        .then(response => {
            console.log(JSON.stringify(response))
            if (!response.ok) {
                throw new Error('Network error');
            }
            return response.json();
        })
        .then(data => {
            this.questions = data.questions;
        }).catch(error => {
            console.error("Couldn't fetch data:", error);
        });
    }
    
    getQuestionsByTags(tag: string) {
        if(!this.selectedTags.includes(tag)) {
            this.selectedTags.push(tag)
        }
        else {
            this.selectedTags = this.selectedTags.filter(function(item) {
                return item !== tag
            })
        }
        
        if(this.selectedTags.length === 0) {
            this.fetchQuestions()
            return;
        }

        fetch(`${this.baseUrl}/getByTags`, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({tags: this.selectedTags}),

        })
        .then(response => {
            console.log(JSON.stringify(response))
            if (!response.ok) {
                throw new Error('Network error');
            }
            return response.json();
        })
        .then(data => {
            this.questions = data
        }).catch(error => {
            console.error("Couldn't fetch data:", error);
        });
    }
    

    searchQuestions(event: KeyboardEvent) {
        if (event.key === 'Enter') {
            const query = (event.target as HTMLInputElement).value;
            if (!query) {
                this.fetchQuestions();
                return;
            }

            fetch(`${this.baseUrl}/search?query=${query}`, {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                },
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network error');
                }
                return response.json();
            })
            .then(data => {
                this.questions = data;
            })
            .catch(error => {
                console.error("Couldn't fetch data:", error);
            });
        }
    }

    upvotePost(post: any) {
        if(post.author == 1) { // change to localstorage
            
        }
        let response;
        fetch(`${this.baseUrl}/vote`, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userId: localStorage.getItem("userId"),
                postId: post.id,
                voteWeight: 1,
            })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network error');
            }
            return response.json();
        }).then(data =>  {
                post.score = +post.score + +data.status;
        }).catch(error => {
            console.error("Couldn't fetch data:", error);
        });
    }

    downvotePost(post: any): void {
        if(post.author == 1) { // change to localstorage
            
        }
        let response;
        fetch(`${this.baseUrl}/vote`, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userId: localStorage.getItem("userId"),
                postId: post.id,
                voteWeight: -1,
            })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network error');
            }
            return response.json();
        }).then(data =>  {
                post.score = +post.score + +data.status;
        }).catch(error => {
            console.error("Couldn't fetch data:", error);
        });
    }

    addQuestion(): void {
        this.newQuestion.userId = localStorage.getItem("userId") as string
        console.log(this.newQuestion.tags.split(" "))
        fetch(`${this.baseUrl}/ask`, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                title: this.newQuestion.title,
                text: this.newQuestion.text,
                userId: localStorage.getItem("userId"),
                tags: this.newQuestion.tags.split(" "),
                picturePath: ""
            })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network error');
            }
            return response.json();
        })
        .then(data => {
            this.questions.push(data);
            this.newQuestion = { 
                title: '',
                text: '',
                userId: localStorage.getItem("userId") as string,
                tags: "",
                picturePath: '',
            };
            this.fetchQuestions();
        })
        .catch(error => {
            console.error("Couldn't fetch data:", error);
        });
        

    }

    deleteQuestion(question: any): void {
        this.newQuestion.userId = localStorage.getItem("userId") as string
        fetch(`${this.baseUrl}/delete?id=${question.id}`, {
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json',
            },
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network error');
            }
            return response.json();
        })
        .then(data => {
            this.fetchQuestions();
        })
        .catch(error => {
            console.error("Couldn't fetch data:", error);
        });
        
    }
}