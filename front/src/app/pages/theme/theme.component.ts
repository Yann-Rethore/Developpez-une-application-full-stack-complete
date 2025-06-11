import { Component, OnInit } from '@angular/core';
import { TopicService } from '../../services/topic.service';
import { TopicDto } from '../../interfaces/topic.dto';

@Component({
  selector: 'app-theme',
  templateUrl: './theme.component.html',
  styleUrls: ['./theme.component.scss']
})
export class ThemeComponent implements OnInit {
  topics: TopicDto[] = [];

  constructor(private topicService: TopicService) {}

  ngOnInit(): void {
    this.topicService.getAllTopics().subscribe(data => {
      this.topics = data;
    });
  }
}