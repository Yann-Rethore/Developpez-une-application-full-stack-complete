import { Component, OnInit } from '@angular/core';
import { TopicService } from '../../services/topic.service';
import { TopicDto } from '../../interfaces/topic.dto';
import { Observable, BehaviorSubject, combineLatest, map, switchMap, tap } from 'rxjs';

@Component({
  selector: 'app-theme',
  templateUrl: './theme.component.html',
  styleUrls: ['./theme.component.scss']
})
export class ThemeComponent implements OnInit {
  private refresh$ = new BehaviorSubject<void>(undefined);

  topics$!: Observable<TopicDto[]>;
  subscribedTopicIds$!: Observable<number[]>;

  constructor(private topicService: TopicService) {}

  ngOnInit(): void {
    this.topics$ = this.refresh$.pipe(
      switchMap(() => this.topicService.getAllTopics())
    );
    this.subscribedTopicIds$ = this.refresh$.pipe(
      switchMap(() => this.topicService.getUserSubscriptions())
    );
  }

  isSubscribed(topicId: number, subscribedIds: number[]): boolean {
    return subscribedIds.includes(Number(topicId));
  }

  toggleSubscription(topicId: number, subscribedIds: number[]) {
    if (this.isSubscribed(topicId, subscribedIds)) {
      // Ne rien faire, bouton désactivé
      return;
    }
    this.topicService.subscribeToTopic(topicId).subscribe(() => {
      this.refresh$.next();
    });
  }
}