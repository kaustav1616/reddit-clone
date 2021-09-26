import { Component, Input, OnInit } from '@angular/core';
import { PostModel } from '../post-model';
import { faComments } from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';

@Component({
	selector: 'app-post-tile',
	templateUrl: './post-tile.component.html',
	styleUrls: ['./post-tile.component.css']
})
export class PostTileComponent implements OnInit
{
	@Input()
	posts: Array<PostModel>;

	faComments = faComments;

	constructor(private router: Router) 
	{
		this.posts = [];
	}

	ngOnInit(): void
	{
	}

	goToPost(postId: number)
	{
		this.router.navigateByUrl('/view-post/' + postId);
	}
}
