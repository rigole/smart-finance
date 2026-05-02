import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
   constructor(private translate: TranslateService) {
    translate.addLangs(['en', 'fr']);
    const savedLang = localStorage.getItem('lang') || 'en';
    translate.setDefaultLang('en');
    translate.use(savedLang);
  }
}
