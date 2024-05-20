import {Component} from '@angular/core';
import {MatCardModule} from '@angular/material/card';
import {NgFor} from '@angular/common';
import {MatInputModule} from '@angular/material/input';

@Component({
    selector: 'app-questions',
    standalone: true,
    imports: [MatCardModule, NgFor, MatInputModule],
    templateUrl: './questions.component.html',
    styleUrl: './questions.component.scss'
})
export class QuestionsComponent {
    private baseUrl: string = "http://localhost:8080/posts";
    private selectedTags: string[] = [];
    questions: any[] = []
    ngOnInit(): void {
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

    async upvotePost(post: any) {
        if(post.author == 1) { // change to localstorage
            
        }
        let response;
        fetch(`${this.baseUrl}/vote`, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userId: "2",
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
                userId: "2",
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
}
