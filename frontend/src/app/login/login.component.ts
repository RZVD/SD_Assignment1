import { MatFormFieldModule } from '@angular/material/form-field';
import { NgModel } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {MatCard, MatCardModule} from '@angular/material/card';
import {NgFor, NgIf} from '@angular/common';
import {MatDialogActions, MatDialogContent, MatDialogTitle, MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatInputModule} from '@angular/material/input';
import { publishFacade } from '@angular/compiler';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  standalone: true,
    imports: [
        MatFormFieldModule,
        MatInputModule,
        MatCardModule,
        FormsModule
  ],
})
export class LoginComponent {
    private baseUrl: string = "http://localhost:8080/users/login"
    public loginData = {username: "", password: ""};

    login(){
        console.log(JSON.stringify(this.loginData))
        fetch(`${this.baseUrl}`, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(this.loginData)
        })
        .then(response => {
            console.log(JSON.stringify(response))
            if (!response.ok) {
                alert("wrong credentials")
                throw new Error('Network error');
            }
            return response.json();
        })
        .then(data => {
            localStorage.setItem("userId", data.userId)
            localStorage.setItem("user", JSON.stringify(data))
            console.log(data)
            window.location.replace("/questions")
        }).catch(error => {
            console.error("Couldn't fetch data:", error);
        });
    }
}
