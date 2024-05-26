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
import { RouterLink } from '@angular/router';



export type Question = {
    title: string,
    text: string,
    userId: string,
    tags: string,
    picturePath?: File
    editMode?: boolean
    image?: string,
}

@Component({
    selector: 'app-questions',
    standalone: true,
    imports: [MatCardModule, NgFor,NgIf, MatInputModule, FormsModule, MatFormFieldModule, MatDialogTitle, MatDialogActions, MatDialogContent, RouterLink],
    templateUrl: './questions.component.html',
    styleUrl: './questions.component.scss'
})

export class QuestionsComponent {
    private baseUrl: string = "http://localhost:8080/posts";
    private selectedTags: string[] = [];
    public userId = localStorage.getItem("userId")
    public user = JSON.parse(localStorage.getItem("user")!)
    public admin: any;
    questions: any[] = []
    editedQuestion: any = null;
    editedAnswer: any = null;
    
    newQuestion: Question = { title: '', text: '', userId: '', tags: '' } ;
    constructor(
        private router: Router
    ){}

    ngOnInit(): void {
        console.log(this.user)
        this.admin = this.user.roles.includes("MODERATOR");
        if (!this.userId) {
          this.router.navigate(['/login']);
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
            this.questions = data.questions.map((question: any) => ({
                ...question,
                image: this.arrayBufferToBase64(question.image)
            }));
        }).catch(error => {
            console.error("Couldn't fetch data:", error);
        });
    }

    arrayBufferToBase64(buffer: ArrayBuffer) {
        let binary = '';
        const bytes = new Uint8Array(buffer);
        for (let i = 0; i < bytes.byteLength; i++) {
            binary += String.fromCharCode(bytes[i]);
        }
        return 'data:image/jpeg;base64,' + btoa(binary);
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
            for (let index = 0; index < (data as Question[]).length; index++) {
                const element = data[index].editMode = false;
                
            }
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
                picturePath: this.newQuestion.picturePath
            })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network error');
            }
            return response.json();
        })
        .then(data => {
            console.log(data)
            this.questions.push(data);
            this.newQuestion = { 
                title: '',
                text: '',
                userId: localStorage.getItem("userId") as string,
                tags: "",
            };
            this.newQuestion.picturePath
            this.fetchQuestions();
        })
        .catch(error => {
            console.error("Couldn't fetch data:", error);
        });

    }
    saveAnswer() {
        fetch(`${this.baseUrl}/update`, {
            method: "PUT",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                ...this.editedAnswer,
                postId: this.editedAnswer.id,
                userId: this.userId,
            })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network error');
            }
            return response.json();
        })
        .then(data => {
            this.fetchQuestions();
            this.editedAnswer = null;
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
    editQuestion(question: any) {
        this.editedQuestion = {...question};
        console.log(this.editedQuestion)
    }

    saveQuestion() {
        console.log(this.editedQuestion)
        fetch(`${this.baseUrl}/update`, {
            method: "PUT",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                ...this.editedQuestion,
                postId: this.editedQuestion.id,
                userId: this.userId,
            })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network error');
            }
            return response.json();
        })
        .then(data => {
            this.fetchQuestions();
            this.editedQuestion = null;
        })
        .catch(error => {
            console.error("Couldn't fetch data:", error);
        });
    }

    cancelEditQuestion() {
        this.editedQuestion = null;
    }

    cancelEditAnswer() {
        this.editedAnswer = null;
    }

    editAnswer(answer: any) {
        this.editedAnswer = { ...answer };
    }
    

    dataURLtoBlob(dataURL: string | ArrayBuffer | null): Blob {
        if (typeof dataURL === 'string') {
            const byteString = atob(dataURL.split(',')[1]);
            const mimeString = dataURL.split(',')[0].split(':')[1].split(';')[0];
            const ab = new ArrayBuffer(byteString.length);
            const ia = new Uint8Array(ab);
            for (let i = 0; i < byteString.length; i++) {
                ia[i] = byteString.charCodeAt(i);
            }
            return new Blob([ab], { type: mimeString });
        }
        throw new Error('Invalid dataURL format');
    }

    readImage(file: File) {
        const reader = new FileReader();
        reader.onload = (e: any) => {
            // Set the data URL as the picturePath
            this.newQuestion.picturePath = e.target.result;
        };
        reader.readAsDataURL(file); // Read the file as a data URL
    }

    onFileSelected(event: any) {
        if (event.target.files && event.target.files.length > 0) {
            const file = event.target.files[0];
            const reader = new FileReader();
            reader.onload = (e: any) => {
                const base64String = e.target.result.split(',')[1]; // Strip the data URL prefix
                this.newQuestion.picturePath = base64String;
            };
            reader.readAsDataURL(file);
        }
    }
}