import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { NgFor } from '@angular/common';

@Component({
    selector: 'app-questions',
    standalone: true,
    imports: [MatCardModule, NgFor],
    templateUrl: './questions.component.html',
    styleUrl: './questions.component.scss'
})
export class QuestionsComponent {
    questions = [
    {
      "title": "How to deploy spring boot app to kubernetes",
      "text": "Same as above",
      "score": 0,
      "voted": null,      
      "answers": [],
      "date": "2024-03-27 18:33:04.443",
      "tags": [
        "JAVA",
        "SPRING",
        "DEVOPS"
      ]
    },
    {
      "title": "How to center a div",
      "id": "1",
      "text": "How to center a div in this framework",
      "score": 0,
      "answers": [
        {
          "text": "This is how you do it",
          "date": "2024-03-27 10:14:09.351",
          "score": 0,
          "author": "eyeberg"
        }, 
        {
          "text": "And this is a better way",
          "date": "2024-03-27 10:14:09.355",
          "score": 0,
          "author": "RZVD"
        }
      ],
      "date": "2024-03-27 10:12:28.838",
      "tags": [
        "JAVA",
        "SPRING",
        "DEVOPS"
      ]
    }
  ]

  upvotePost(post: any): void{
    if(!post.voted) {
        post.score += 1;
        post.voted="up";
    }
    else if (post.voted === "down") {
        post.score += 2;
        post.voted = "up";
    }
  }

  downvotePost(post: any): void{
    if(!post.voted) {
        post.score -= 1;
        post.voted="down";
    }
    else if (post.voted === "up") {
        post.score -= 2;
        post.voted = "down";
    }
  }
}
