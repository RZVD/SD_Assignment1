import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {MatCardModule} from '@angular/material/card';
import {NgFor, NgIf} from '@angular/common';

@Component({
    standalone: true,
    selector: 'app-user',
    imports: [MatCardModule, NgFor, NgIf],
    templateUrl: './user.component.html',
    styleUrl: './user.component.scss'
})
export class UserComponent implements OnInit{
    public userId: any;
    public user: any;
    private baseUrl: string = "http://localhost:8080/users"
    constructor(private route: ActivatedRoute) { }
    ngOnInit(): void {
        this.userId = this.route.snapshot.paramMap.get("id")!;
        this.getUser();
    }
    getUser() {
        fetch(`${this.baseUrl}/get?id=${this.userId}`, {
            method: "GET",
            headers: {
            'Content-Type': 'application/json',
        }}).then(response => {
            if (!response.ok) {
                throw new Error('Network error');
            }
            return response.json();
        }).then(data =>  {
                this.user = data
        }).catch(error => {
            console.error("Couldn't fetch data:", error);
        });
    }
    deleteUser() {
        fetch(`${this.baseUrl}/deleteUser?id=${this.userId}`, {
            method: "DELETE",
            headers: {
            'Content-Type': 'application/json',
        }}).then(response => {
            if (!response.ok) {
                throw new Error('Network error');
            }
            return response.json();
        }).then(data =>  {
            window.location.replace("/questions")
        }).catch(error => {
            console.error("Couldn't fetch data:", error);
        });
    }

}
