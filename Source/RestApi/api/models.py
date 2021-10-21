from django.db import models

# Create your models here.
class Device(models.Model):
	id = models.IntegerField(primary_key=True)
	data1 = models.IntegerField(default = 0)
	data2 = models.IntegerField(default = 0)
	data3 = models.IntegerField(default = 0)