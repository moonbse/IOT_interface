from django.conf.urls import url
from api import views

urlpatterns = [
    url(r'^api/devices$', views.device_list),
    url(r'^api/devices/(?P<pk>[0-9]+)$', views.device_detail),
]