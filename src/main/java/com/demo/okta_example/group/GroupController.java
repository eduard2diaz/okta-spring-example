package com.demo.okta_example.group;

import com.okta.sdk.client.Client;
import com.okta.sdk.resource.group.Group;
import com.okta.sdk.resource.group.GroupBuilder;
import com.okta.sdk.resource.group.GroupList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private Client client;

    @GetMapping("/list")
    public GroupList listGroups() {
        return client.listGroups();
    }

    @GetMapping("/search")
    public GroupList searchGroups(@RequestParam String query) {
        return client.listGroups();
    }

    @PostMapping("/create")
    public Group createGroup(@RequestBody GroupDto groupDto) {
        return GroupBuilder.instance()
                .setName(groupDto.name())
                .setDescription(groupDto.description())
                .buildAndCreate(client);
    }

    @PutMapping("/{groupId}/update")
    public Group updateGroup(@PathVariable String groupId, @RequestBody GroupDto groupDto) throws Exception {
        Group group = client.getGroup(groupId);
        if (Objects.isNull(group)) {
            throw new Exception("Group not found");
        }

        if (groupDto.name() != null) {
            group.getProfile().setName(groupDto.name());
        }
        if (groupDto.description() != null) {
            group.getProfile().setDescription(groupDto.description());
        }

        group.update();
        return group;
    }

    @DeleteMapping("/{groupId}/delete")
    public void deleteGroup(@PathVariable String groupId) throws Exception {
        Group group = client.getGroup(groupId);
        if (Objects.isNull(group)) {
            throw new Exception("Group not found");
        }
        group.delete();
    }
}

