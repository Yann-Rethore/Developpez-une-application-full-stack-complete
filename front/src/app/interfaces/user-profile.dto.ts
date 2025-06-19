import { TopicDto } from './topic.dto';

export interface UserProfileDTO {
  username: string;
  email: string;
  abonnements: TopicDto[];
}

export interface UserProfileUpdateDTO {
  username?: string;
  email?: string;
  password?: string;
  desabonnements?: number[];
}