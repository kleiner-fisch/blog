package com.example.demo.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.example.demo.controller.UserController;
import com.example.demo.model.CustomUser;

@Component
public class UserDTOAssembler 
  extends RepresentationModelAssemblerSupport<CustomUser, UserDTO> {

    public UserDTOAssembler() {
        // Class<> controllerClass, Class<> resourceType
        super(UserController.class, UserDTO.class);
        //TODO Auto-generated constructor stub
    }

    @Override
    public UserDTO toModel(CustomUser entity) {
        UserDTO result = new UserDTO(entity);
        result.add(WebMvcLinkBuilder.linkTo(getControllerClass()).slash(result.getUserId()).withSelfRel());
        return result;
    }
 
//   public UserDTOAssembler() {
//     super(WebController.class, UserDTO.class);
//   }
 
//   @Override
//   public UserDTO toModel(CustomUser entity) 
//   {
//     UserDTO albumModel = instantiateModel(entity);
     
//     albumModel.add(linkTo(
//         methodOn(WebController.class)
//         .getActorById(entity.getId()))
//         .withSelfRel());
     
//     albumModel.setId(entity.getId());
//     albumModel.setTitle(entity.getTitle());
//     albumModel.setDescription(entity.getDescription());
//     albumModel.setReleaseDate(entity.getReleaseDate());
//     albumModel.setActors(toActorModel(entity.getActors()));
//     return albumModel;
//   }
   
//   @Override
//   public CollectionModel<AlbumModel> toCollectionModel(Iterable<? extends AlbumEntity> entities) 
//   {
//     CollectionModel<AlbumModel> actorModels = super.toCollectionModel(entities);
     
//     actorModels.add(linkTo(methodOn(WebController.class).getAllAlbums()).withSelfRel());
     
//     return actorModels;
//   }
 
//   private List<ActorModel> toActorModel(List<ActorEntity> actors) {
//     if (actors.isEmpty())
//       return Collections.emptyList();
 
//     return actors.stream()
//         .map(actor -> ActorModel.builder()
//             .id(actor.getId())
//             .firstName(actor.getFirstName())
//             .lastName(actor.getLastName())
//             .build()
//             .add(linkTo(
//                 methodOn(WebController.class)
//                 .getActorById(actor.getId()))
//                 .withSelfRel()))
//         .collect(Collectors.toList());
//   }
}