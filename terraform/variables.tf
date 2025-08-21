variable "region" {
  description = "AWS region"
  type        = string
  default     = "ap-south-1"
}

variable "ecr_repo_name" {
  description = "ECR repo name"
  type        = string
  default     = "wayfair-catalog"
}
