import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { UsersComponent } from './users/users.component';
import { QuestionsComponent } from './questions/questions.component';
import { UserComponent } from './user/user.component';

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
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
