import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {MatCard, MatCardModule} from '@angular/material/card';
import {NgFor, NgIf} from '@angular/common';
import {MatDialogActions, MatDialogContent, MatDialogTitle, MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { publishFacade } from '@angular/compiler';

type Question = {}

@Component({
  selector: 'app-question',
  standalone: true,
  imports: [NgIf, NgFor, MatCard, MatCardModule, MatInputModule, FormsModule, MatFormFieldModule],
  templateUrl: './question.component.html',
  styleUrl: './question.component.scss'
})
export class QuestionComponent implements OnInit {
    private baseUrl: string = "http://localhost:8080/posts";
    public questionId: string | undefined;
    public question: any;
    public userId: any;
    public newAnswer = { 
        text: '',
        userId: localStorage.getItem("userId") as string,
        picturePath: '',
        questionId: this.route.snapshot.paramMap.get("id")!
    } ;

    getQuestion() {
        fetch(`${this.baseUrl}/getPost?id=${this.questionId}`, {
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
            this.question = data
        }).catch(error => {
            console.error("Couldn't fetch data:", error);
        });
    }

    constructor(private route: ActivatedRoute) { }
    ngOnInit(): void {
        this.questionId = this.route.snapshot.paramMap.get("id")!;
        this.userId=localStorage.getItem("userId")
        this.getQuestion();
    }
    upvotePost(post: any) {
        if(post.author == localStorage.getItem("userId")) { // change to localstorage
            fetch(`${this.baseUrl}/vote`, {
                method: "POST",
                headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userId: localStorage.getItem("userId"),
                postId: post.id,
                voteWeight: 1,
            })})
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
    }

    downvotePost(post: any): void {
        if(post.author == 1) { // change to localstorage
            
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
            })})
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
    }
    
    addAnswer(){
        this.newAnswer.userId = localStorage.getItem("userId") as string
        console.log(this.newAnswer)
        fetch(`${this.baseUrl}/answer`, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(this.newAnswer)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network error');
            }
            return response.json();
        })
        .then(data => {
            this.getQuestion();
            this.newAnswer = { 
                text: '',
                userId: localStorage.getItem("userId") as string,
                picturePath: '',
                questionId: this.route.snapshot.paramMap.get("id")!
            };
        })
        .catch(error => {
            console.error("Couldn't fetch data:", error);
        });
        
    }

    deleteQuestion(question: any): void {
        this.newAnswer.userId = localStorage.getItem("userId") as string
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
        })
        .catch(error => {
            console.error("Couldn't fetch data:", error);
        });
        
    }
}
    
