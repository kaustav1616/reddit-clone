import { Component, OnInit } from '@angular/core';
import { PostModel } from '../shared/post-model';
import { PostService } from '../shared/post.service';
import { faArrowUp as faArrowUp, faArrowDown as faArrowDown } from '@fortawesome/free-solid-svg-icons';
import { faComments as faComments } from '@fortawesome/free-solid-svg-icons';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';


@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit
{
	posts: PostModel[] = [];

	constructor(private postService: PostService, library: FaIconLibrary) 
	{
		this.postService.getAllPosts().subscribe((data) =>
		{
			this.posts = data;
		});
	}

	ngOnInit(): void
	{
	}
}