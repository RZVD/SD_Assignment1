import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NavbarComponent} from './components/navbar/navbar.component';
import {UsersComponent} from './views/users/users.component';
import {QuestionsComponent} from './views/questions/questions.component';
import {UserComponent} from './views/user/user.component';
import {FormsModule, NgModel} from '@angular/forms';
import { QuestionComponent } from './question/question.component';
import { LoginComponent } from './login/login.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
],
imports: [
    BrowserModule,
    AppRoutingModule,
    UsersComponent,
    QuestionsComponent,
    UserComponent,
    FormsModule,
    BrowserAnimationsModule,
    QuestionComponent,
    LoginComponent,
    FormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
